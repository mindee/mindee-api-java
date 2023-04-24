package com.mindee.pdf;

import lombok.Value;

/**
 * The split PDF.
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
