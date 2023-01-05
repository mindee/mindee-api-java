package com.mindee.parsing;

import lombok.Value;

import java.util.List;

/**
 * Represent options to cut a document.
 */
@Value
public class PageOptions {

  /**
   * List of page indexes.
   * A negative index can be used, indicating an offset from the end of the document.
   * [1, -1] represents the first and last pages of the document.
   */
  List<Integer> pages;
  /**
   * Page operation on a document.
   * KeepOnly by default.
   */
  PageOptionsOperation mode;
  /**
   * Apply the operation only if the document has at least this many pages.
   * 0 by default.
   */
  Integer onMinPages;

  public PageOptions(List<Integer> pages) {
    this(pages, PageOptionsOperation.KEEP_ONLY_LISTED_PAGES, 0);
  }

  public PageOptions(List<Integer> pages, PageOptionsOperation mode) {
    this(pages, mode, 0);
  }

  public PageOptions(List<Integer> pages, PageOptionsOperation mode, Integer onMinPages) {
    this.pages = pages;
    this.mode = mode;
    this.onMinPages = onMinPages;
  }
}
