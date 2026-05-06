package com.mindee.pdf;

import com.mindee.input.PageOptions;
import java.io.IOException;

/**
 * Various operations required for PDF input files.
 */
public interface PDFInputOperation {

  /**
   * Split a PDF file.
   *
   * @param fileBytes A byte array representing a PDF.
   */
  SplitPDF split(byte[] fileBytes, PageOptions pageOptions) throws IOException;

  /**
   * Get the number of pages in a PDF file.
   *
   * @param fileBytes A byte array representing a PDF.
   */
  int getPageCount(byte[] fileBytes) throws IOException;

  /**
   * Returns true if the file is a PDF.
   *
   * @param fileBytes A byte array representing a PDF.
   */
  boolean isPDF(byte[] fileBytes);
}
