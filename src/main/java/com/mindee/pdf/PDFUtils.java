package com.mindee.pdf;

import com.mindee.input.LocalInputSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * Utilities for working with PDFs.
 */
public final class PDFUtils {

  private PDFUtils() {
  }

  private static int countPDDocumentPages(PDDocument document) throws IOException {
    int pageCount = document.getNumberOfPages();
    document.close();
    return pageCount;
  }

  public static int countPdfPages(InputStream inputStream) throws IOException {
    try {
      PDDocument document = PDDocument.load(inputStream);
      int pageCount = countPDDocumentPages(document);
      document.close();
      return pageCount;
    } finally {
      inputStream.close();
    }
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

  public static List<PdfPageImage> pdfToImages(String filePath) throws IOException {
    return pdfToImages(new LocalInputSource(filePath));
  }

  public static List<PdfPageImage> pdfToImages(LocalInputSource source) throws IOException {
    PDDocument document = PDDocument.load(source.getFile());
    PDFRenderer pdfRenderer = new PDFRenderer(document);
    List<PdfPageImage> pdfPageImages = new ArrayList<>();
    for (int i = 0; i < document.getNumberOfPages(); i++) {
      BufferedImage imageBuffer = pdfRenderer.renderImageWithDPI(i, 220, ImageType.RGB);
      pdfPageImages.add(new PdfPageImage(imageBuffer, i, source.getFilename(), "jpg"));
    }
    return pdfPageImages;
  }
}
