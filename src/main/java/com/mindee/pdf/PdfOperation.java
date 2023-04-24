package com.mindee.pdf;

import java.io.IOException;

/**
 * Minimum PDF operations.
 */
public interface PdfOperation {

  /**
   * Split a PDF file.
   *
   * @param splitQuery Options to perform the query.
   * @return The split pdf.
   */
  SplitPdf split(SplitQuery splitQuery) throws IOException;
}
