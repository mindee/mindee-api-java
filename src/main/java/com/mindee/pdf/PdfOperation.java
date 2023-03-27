package com.mindee.pdf;

import java.io.IOException;

public interface PdfOperation {

  /**
   * To split a pdf file.
   *
   * @param splitQuery Options to perform the query.
   * @return The split pdf.
   */
  SplitPdf split(SplitQuery splitQuery) throws IOException;
}
