package com.mindee;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Options to pass to the `parse` method for cutting multipage documents.
 */
@Value
@Builder
public class PageOptions {

  /**
   * Operation to apply on the document, given the pages specified.
   */
  public enum PageOptionsOperation {
    KEEP_ONLY_LISTED_PAGES,
    REMOVE_LISTED_PAGES
  }

  private PageOptionsOperation mode;
  private List<Integer> pages;
  @Builder.Default
  private Integer onMinPages = 10;

}
