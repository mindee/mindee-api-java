package com.mindee.parsing;

import java.util.List;
import lombok.Value;

/**
 * Represent options to cut a document.
 */
@Value
public class PageOptions {

  /**
   * Zero-based list of page indexes.
   * A negative index can be used, indicating an offset from the end of the document.
   * [1, -1] represents the first and last pages of the document.
   */
  List<Integer> pageIndexes;
  /**
   * Operation to apply on the document, given the `pageIndexes` specified.
   * `KeepOnly` by default.
   */
  PageOptionsOperation operation;
  /**
   * Apply the operation only if the document has at least this many pages.
   * 0 by default.
   */
  Integer onMinPages;

  public PageOptions(List<Integer> pages) {
    this(pages, PageOptionsOperation.KEEP_ONLY, 0);
  }

  public PageOptions(List<Integer> pages, PageOptionsOperation operation) {
    this(pages, operation, 0);
  }

  public PageOptions(List<Integer> pageIndexes, PageOptionsOperation operation, Integer onMinPages) {
    this.pageIndexes = pageIndexes;
    this.operation = operation;
    this.onMinPages = onMinPages;
  }
}
