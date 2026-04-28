package com.mindee.pdf;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;

public interface PDFExtraction {
//  /**
//   * Render a single page of a PDF as an image.
//   */
//  PdfPageImage pdfPageToImage(byte[] fileBytes, String filename, int pageNumber) throws IOException;
//
//  default PdfPageImage pdfPageToImage(LocalInputSource source, int pageNumber) throws IOException {
//    return pdfPageToImage(source.getFile(), source.getFilename(), pageNumber);
//  }

//  /**
//   * Render all pages of a PDF as images.
//   */
//  List<PdfPageImage> pdfToImages(byte[] fileBytes, String filename) throws IOException;
//
//  default List<PdfPageImage> pdfToImages(LocalInputSource source) throws IOException {
//    return pdfToImages(source.getFile(), source.getFilename());
//  }

  public byte[] mergePdfPages(File file, List<Integer> pageNumbers) throws IOException;

  default byte[] mergePdfPages(PDDocument document, List<Integer> pageNumbers) throws IOException {
    return mergePdfPages(document, pageNumbers, true);
  }

  public byte[] mergePdfPages(
      PDDocument document,
      List<Integer> pageNumbers,
      boolean closeOriginal
  ) throws IOException;
}
