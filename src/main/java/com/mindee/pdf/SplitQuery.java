package com.mindee.pdf;

import com.mindee.parsing.PageOptions;
import lombok.Value;

/**
 * Represent parameter to split a PDF.
 */
@Value
public class SplitQuery {

  /**
   * The file.
   */
  byte[] file;
  /**
   * Represent options to cut a document.
   */
  PageOptions pageOptions;
}
