package com.mindee.pdf;

import com.mindee.input.LocalInputSource;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

/**
 * Utilities for working with PDFs.
 */
public final class PDFUtils {

  private PDFUtils() {
  }

  /**
   * Get the number of pages in the PDF.
   *
   * @param inputSource The PDF file.
   */
  public static int getNumberOfPages(LocalInputSource inputSource) throws IOException {
    PDDocument document = Loader.loadPDF(inputSource.getFile());
    int pageCount = document.getNumberOfPages();
    document.close();
    return pageCount;
  }

  private static PDPage clonePage(PDPage page) {

    COSDictionary pageDict = page.getCOSObject();
    COSDictionary newPageDict = new COSDictionary(pageDict);

    newPageDict.removeItem(COSName.ANNOTS);

    return new PDPage(newPageDict);
  }

  private static byte[] createPdfFromExistingPdf(
      PDDocument document, List<Integer> pageNumbers,
      boolean closeOriginal
  ) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PDDocument newDocument = new PDDocument();
    int pageCount = document.getNumberOfPages();
    pageNumbers.stream().filter(i -> i < pageCount)
        .forEach(i -> newDocument.addPage(clonePage(document.getPage(i))));

    newDocument.save(outputStream);
    newDocument.close();
    if (closeOriginal) {
      document.close();
    }

    byte[] output = outputStream.toByteArray();
    outputStream.close();
    return output;
  }

  /**
   * Merge specified PDF pages together.
   *
   * @param file        The PDF file.
   * @param pageNumbers Lit of page numbers to merge together.
   */
  public static byte[] mergePdfPages(File file, List<Integer> pageNumbers) throws IOException {
    PDDocument document = Loader.loadPDF(file);
    return createPdfFromExistingPdf(document, pageNumbers, true);
  }

  public static byte[] mergePdfPages(PDDocument document, List<Integer> pageNumbers)
      throws IOException {
    return mergePdfPages(document, pageNumbers, true);
  }


  public static byte[] mergePdfPages(
      PDDocument document, List<Integer> pageNumbers,
      boolean closeOriginal
  ) throws IOException {
    return createPdfFromExistingPdf(document, pageNumbers, closeOriginal);
  }


  public static boolean isPdfEmpty(File file) throws IOException {
    return checkIfPdfIsEmpty(Loader.loadPDF(file));
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

      if (xObjects.spliterator().getExactSizeIfKnown() != 0
          || fonts.spliterator().getExactSizeIfKnown() != 0) {
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
   *
   * @param filePath The path to the PDF file.
   * @return List of all pages as images.
   */
  public static List<PdfPageImage> pdfToImages(String filePath) throws IOException {
    return pdfToImages(new LocalInputSource(filePath));
  }

  /**
   * Render all pages of a PDF as images.
   * Converting PDFs with hundreds of pages may result in a heap space error.
   *
   * @param source The PDF file.
   * @return List of all pages as images.
   */
  public static List<PdfPageImage> pdfToImages(LocalInputSource source) throws IOException {
    PDDocument document = Loader.loadPDF(source.getFile());
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
   *
   * @param filePath   The path to the PDF file.
   * @param pageNumber The page number to render, first page is 1.
   * @return The page as an image.
   */
  public static PdfPageImage pdfPageToImage(String filePath, int pageNumber) throws IOException {
    return pdfPageToImage(new LocalInputSource(filePath), pageNumber);
  }

  /**
   * Render a single page of a PDF as an image.
   * Main use case is for processing PDFs with hundreds of pages.
   * If you need to only render some pages from the PDF, use <code>mergePdfPages</code> and
   * then <code>pdfToImages</code>.
   *
   * @param source     The PDF file.
   * @param pageNumber The page number to render, first page is 1.
   * @return The page as an image.
   */
  public static PdfPageImage pdfPageToImage(
      LocalInputSource source,
      int pageNumber
  ) throws IOException {
    int index = pageNumber - 1;
    PDDocument document = Loader.loadPDF(source.getFile());
    PDFRenderer pdfRenderer = new PDFRenderer(document);
    BufferedImage imageBuffer = pdfPageToImageBuffer(index, document, pdfRenderer);
    document.close();
    return new PdfPageImage(imageBuffer, index, source.getFilename(), "jpg");
  }

  private static BufferedImage pdfPageToImageBuffer(
      int index, PDDocument document,
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

  public static byte[] documentToBytes(PDDocument document) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    document.save(outputStream);
    return outputStream.toByteArray();
  }


  public static void extractAndAddText(
      PDDocument inputDoc, PDPageContentStream contentStream,
      int pageIndex, boolean disableSourceText
  ) throws IOException {
    if (disableSourceText) {
      return;
    }

    PDFTextStripper stripper = new PDFTextStripper() {
      @Override
      protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        if (textPositions.isEmpty()) {
          return;
        }

        TextPosition firstPosition = textPositions.get(0);
        float fontSize = firstPosition.getFontSizeInPt();
        PDColor color = getGraphicsState().getNonStrokingColor();
        contentStream.beginText();
        contentStream.setFont(firstPosition.getFont(), fontSize);
        contentStream.setNonStrokingColor(convertToAwtColor(color));

        float x = firstPosition.getXDirAdj();
        float y = firstPosition.getPageHeight() - firstPosition.getYDirAdj();

        contentStream.newLineAtOffset(x, y);
        try {
          contentStream.showText(text);
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
          contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), fontSize);
          contentStream.showText(text);
        }
        contentStream.endText();
      }
    };

    stripper.setStartPage(pageIndex + 1);
    stripper.setEndPage(pageIndex + 1);
    stripper.getText(inputDoc);
  }

  private static Color convertToAwtColor(PDColor pdColor) {
    float[] components = pdColor.getComponents();
    if (components.length == 1) {
      // Grayscale
      return new Color(components[0], components[0], components[0]);
    } else if (components.length == 3) {
      // RGB
      return new Color(components[0], components[1], components[2]);
    } else if (components.length == 4) {
      // CMYK (simplified conversion)
      float c = components[0];
      float m = components[1];
      float y = components[2];
      float k = components[3];
      float r = 1 - Math.min(1, c + k);
      float g = 1 - Math.min(1, m + k);
      float b = 1 - Math.min(1, y + k);
      return new Color(r, g, b);
    }
    return Color.BLACK;
  }

  public static void addImageToPage(
      PDPageContentStream contentStream, PDImageXObject pdImage,
      PDRectangle pageSize
  ) throws IOException {
    contentStream.drawImage(pdImage, 0, 0, pageSize.getWidth(), pageSize.getHeight());
  }
}
