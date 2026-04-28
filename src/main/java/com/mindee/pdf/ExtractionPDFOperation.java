package com.mindee.pdf;

import com.mindee.input.LocalInputSource;

import java.io.IOException;
import java.util.List;

public interface ExtractionPDFOperation {
//  /**
//   * Render a single page of a PDF as an image.
//   */
//  PdfPageImage pdfPageToImage(byte[] fileBytes, String filename, int pageNumber) throws IOException;
//
//  default PdfPageImage pdfPageToImage(LocalInputSource source, int pageNumber) throws IOException {
//    return pdfPageToImage(source.getFile(), source.getFilename(), pageNumber);
//  }

  /**
   * Render all pages of a PDF as images.
   */
  List<PdfPageImage> pdfToImages(byte[] fileBytes, String filename) throws IOException;

  default List<PdfPageImage> pdfToImages(LocalInputSource source) throws IOException {
    return pdfToImages(source.getFile(), source.getFilename());
  }
}
