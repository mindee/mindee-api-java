package com.mindee.input;

import java.util.Arrays;
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
   * [0, -1] represents the first and last pages of the document.
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

  /**
   * Default constructor.
   * 
   * @deprecated Use the Builder pattern instead.
   */
  @Deprecated
  public PageOptions(List<Integer> pages) {
    this(pages, PageOptionsOperation.KEEP_ONLY, 0);
  }

  /**
   * Constructor with operation.
   * 
   * @deprecated Use the Builder pattern instead.
   */
  @Deprecated
  public PageOptions(List<Integer> pages, PageOptionsOperation operation) {
    this(pages, operation, 0);
  }

  public PageOptions(
      List<Integer> pageIndexes,
      PageOptionsOperation operation,
      Integer onMinPages
  ) {
    this.pageIndexes = pageIndexes;
    this.operation = operation;
    this.onMinPages = onMinPages;
  }

  /**
   * Builder for page options.
   */
  public static final class Builder {
    private List<Integer> pageIndexes;
    private PageOptionsOperation operation;
    private Integer onMinPages = 0;

    public Builder pageIndexes(Integer[] pageIndexes) {
      this.pageIndexes = Arrays.asList(pageIndexes);
      return this;
    }

    public Builder pageIndexes(List<Integer> pageIndexes) {
      this.pageIndexes = pageIndexes;
      return this;
    }

    public Builder operation(PageOptionsOperation operation) {
      this.operation = operation;
      return this;
    }

    public Builder onMinPages(Integer onMinPages) {
      this.onMinPages = onMinPages;
      return this;
    }

    public PageOptions build() {
      return new PageOptions(pageIndexes, operation, onMinPages);
    }
  }
}
