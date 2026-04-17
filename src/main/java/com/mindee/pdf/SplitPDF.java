package com.mindee.pdf;

import lombok.Value;

/**
 * The split PDF.
 */
@Value
public class SplitPDF {

  /**
   * The file.
   */
  byte[] file;
  /**
   * The total page number.
   */
  int totalPageNumber;
}
