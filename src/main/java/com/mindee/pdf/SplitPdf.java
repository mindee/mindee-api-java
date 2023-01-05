package com.mindee.pdf;

import lombok.Value;

/**
 * The split pdf.
 */
@Value
public class SplitPdf {

  /**
   * The file.
   */
  byte[] file;
  /**
   * The total page number.
   */
  int totalPageNumber;
}
