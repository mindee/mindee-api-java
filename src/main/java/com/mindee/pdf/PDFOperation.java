package com.mindee.pdf;

import com.mindee.input.LocalInputSource;
import com.mindee.input.PageOptions;
import java.io.IOException;
import java.util.List;

/**
 * Minimum PDF operations.
 */
public interface PDFOperation {

  /**
   * Split a PDF file.
   */
  SplitPDF split(byte[] fileBytes, PageOptions pageOptions) throws IOException;

  /**
   * Get the number of pages in a PDF file.
   */
  int getNumberOfPages(byte[] fileBytes) throws IOException;

  default int getNumberOfPages(LocalInputSource inputSource) throws IOException {
    return getNumberOfPages(inputSource.getFile());
  }

  /**
   * Render a single page of a PDF as an image.
   */
  PdfPageImage pdfPageToImage(byte[] fileBytes, String filename, int pageNumber) throws IOException;

  default PdfPageImage pdfPageToImage(LocalInputSource source, int pageNumber) throws IOException {
    return pdfPageToImage(source.getFile(), source.getFilename(), pageNumber);
  }

  /**
   * Render all pages of a PDF as images.
   */
  List<PdfPageImage> pdfToImages(byte[] fileBytes, String filename) throws IOException;

  default List<PdfPageImage> pdfToImages(LocalInputSource source) throws IOException {
    return pdfToImages(source.getFile(), source.getFilename());
  }
}
