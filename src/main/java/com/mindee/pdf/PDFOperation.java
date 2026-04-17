package com.mindee.pdf;

import java.io.IOException;

/**
 * Minimum PDF operations.
 */
public interface PDFOperation {

  /**
   * Split a PDF file.
   *
   * @param splitQuery Options to perform the query.
   * @return The split PDF.
   */
  SplitPDF split(SplitQuery splitQuery) throws IOException;
}
