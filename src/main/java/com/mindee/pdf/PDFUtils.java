package com.mindee.pdf;

import com.mindee.input.LocalInputSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * Utilities for working with PDFs.
 */
public final class PDFUtils {

  private PDFUtils() {}

  /**
   * Get the number of pages in the PDF.
   * @param inputSource The PDF file.
   */
  public static int getNumberOfPages(LocalInputSource inputSource) throws IOException {
    PDDocument document = PDDocument.load(inputSource.getFile());
    int pageCount = document.getNumberOfPages();
    document.close();
    return pageCount;
  }

  private static byte[] createPdfFromExistingPdf(
      PDDocument document,
      List<Integer> pageNumbers
  ) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PDDocument newDocument = new PDDocument();
    int pageCount = document.getNumberOfPages();
    pageNumbers.stream()
        .filter(i -> i < pageCount)
        .forEach(i -> newDocument.addPage(document.getPage(i)));

    newDocument.save(outputStream);
    newDocument.close();
    document.close();

    byte[] output = outputStream.toByteArray();
    outputStream.close();
    return output;
  }

  /**
   * Merge specified PDF pages together.
   * @param file The PDF file.
   * @param pageNumbers Lit of page numbers to merge together.
   */
  public static byte[] mergePdfPages(
      File file,
      List<Integer> pageNumbers
  ) throws IOException {
    PDDocument document = PDDocument.load(file);
    return createPdfFromExistingPdf(document, pageNumbers);
  }

  public static boolean isPdfEmpty(File file) throws IOException {
    return checkIfPdfIsEmpty(PDDocument.load(file));
  }

  private static boolean checkIfPdfIsEmpty(PDDocument document) throws IOException {
    boolean isEmpty = true;
    for (PDPage page : document.getPages()) {
      PDResources resources = page.getResources();
      if (resources == null) {
        continue;
      }
      Iterable<COSName> xObjects = resources.getXObjectNames();
      Iterable<COSName> fonts = resources.getFontNames();

      if (
          xObjects.spliterator().getExactSizeIfKnown() != 0
          || fonts.spliterator().getExactSizeIfKnown() != 0
      ) {
        isEmpty = false;
        break;
      }
    }
    document.close();

    return isEmpty;
  }

  /**
   * Render all pages of a PDF as images.
   * Converting PDFs with hundreds of pages may result in a heap space error.
   * @param filePath The path to the PDF file.
   * @return List of all pages as images.
   */
  public static List<PdfPageImage> pdfToImages(String filePath) throws IOException {
    return pdfToImages(new LocalInputSource(filePath));
  }

  /**
   * Render all pages of a PDF as images.
   * Converting PDFs with hundreds of pages may result in a heap space error.
   * @param source The PDF file.
   * @return List of all pages as images.
   */
  public static List<PdfPageImage> pdfToImages(LocalInputSource source) throws IOException {
    PDDocument document = PDDocument.load(source.getFile());
    PDFRenderer pdfRenderer = new PDFRenderer(document);
    List<PdfPageImage> pdfPageImages = new ArrayList<>();
    for (int i = 0; i < document.getNumberOfPages(); i++) {
      BufferedImage imageBuffer = pdfPageToImageBuffer(i, document, pdfRenderer);
      pdfPageImages.add(new PdfPageImage(imageBuffer, i, source.getFilename(), "jpg"));
    }
    document.close();
    return pdfPageImages;
  }

  /**
   * Render a single page of a PDF as an image.
   * Main use case is for processing PDFs with hundreds of pages.
   * If you need to only render some pages from the PDF, use <code>mergePdfPages</code> and then <code>pdfToImages</code>.
   * @param filePath The path to the PDF file.
   * @param pageNumber The page number to render, first page is 1.
   * @return The page as an image.
   */
  public static PdfPageImage pdfPageToImage(String filePath, int pageNumber) throws IOException {
    return pdfPageToImage(new LocalInputSource(filePath), pageNumber);
  }

  /**
   * Render a single page of a PDF as an image.
   * Main use case is for processing PDFs with hundreds of pages.
   * If you need to only render some pages from the PDF, use <code>mergePdfPages</code> and then <code>pdfToImages</code>.
   * @param source The PDF file.
   * @param pageNumber The page number to render, first page is 1.
   * @return The page as an image.
   */
  public static PdfPageImage pdfPageToImage(
      LocalInputSource source,
      int pageNumber
  ) throws IOException {
    int index = pageNumber - 1;
    PDDocument document = PDDocument.load(source.getFile());
    PDFRenderer pdfRenderer = new PDFRenderer(document);
    BufferedImage imageBuffer = pdfPageToImageBuffer(index, document, pdfRenderer);
    document.close();
    return new PdfPageImage(imageBuffer, index, source.getFilename(), "jpg");
  }

  private static BufferedImage pdfPageToImageBuffer(
      int index,
      PDDocument document,
      PDFRenderer pdfRenderer
  ) throws IOException {
    PDRectangle bbox = document.getPage(index).getBBox();
    float dimension = bbox.getWidth() * bbox.getHeight();
    int dpi;
    if (dimension < 200000) {
      dpi = 300;
    } else if (dimension < 300000) {
      dpi = 250;
    } else {
      dpi = 200;
    }
    return pdfRenderer.renderImageWithDPI(index, dpi, ImageType.RGB);
  }
}
