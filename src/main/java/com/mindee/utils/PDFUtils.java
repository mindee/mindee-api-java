package com.mindee.utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;

public final class PDFUtils {

  private PDFUtils() {
  }

  private static int countPDDocumentPages(PDDocument document) throws IOException {
    int pageCount = document.getNumberOfPages();
    document.close();
    return pageCount;
  }

  public static int countPdfPages(File file) throws IOException {
    PDDocument document = PDDocument.load(file);
    return countPDDocumentPages(document);
  }

  public static int countPdfPages(byte[] bytes) throws IOException {
    PDDocument document = PDDocument.load(bytes);
    return countPDDocumentPages(document);
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

  private static byte[] createPdfFromExistingPDF(PDDocument document, List<Integer> pageNumbers)
      throws IOException {
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

  public static byte[] mergePdfPages(File file, List<Integer> pageNumbers) throws IOException {
    PDDocument document = PDDocument.load(file);
    return createPdfFromExistingPDF(document, pageNumbers);
  }

  public static byte[] mergePdfPages(byte[] fileBytes, List<Integer> pageNumbers)
      throws IOException {
    PDDocument document = PDDocument.load(fileBytes);
    return createPdfFromExistingPDF(document, pageNumbers);
  }

  public static byte[] mergePdfPages(InputStream inputStream, List<Integer> pageNumbers)
      throws IOException {
    try {
      PDDocument document = PDDocument.load(inputStream);
      byte[] bytes = createPdfFromExistingPDF(document, pageNumbers);
      document.close();
      return bytes;
    } finally {
      inputStream.close();
    }

  }

  public static boolean isPdfEmpty(File file) throws IOException {
    return checkIfPdfIsEmpty(PDDocument.load(file));
  }

  public static boolean isPdfEmpty(InputStream inputStream) throws IOException {
    return checkIfPdfIsEmpty(PDDocument.load(inputStream));
  }

  public static boolean isPdfEmpty(byte[] fileBytes) throws IOException {
    return checkIfPdfIsEmpty(PDDocument.load(fileBytes));
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

  public static boolean checkPdfOpen(InputStream inputStream) {
    boolean opens = false;
    try {
      PDDocument.load(inputStream).close();
      opens = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return opens;
  }

  public static boolean checkPdfOpen(File file) {
    boolean opens = false;
    try {
      PDDocument.load(file).close();
      opens = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return opens;
  }

  public static boolean checkPdfOpen(byte[] fileBytes) {
    boolean opens = false;
    try {
      PDDocument.load(fileBytes).close();
      opens = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return opens;
  }


}
