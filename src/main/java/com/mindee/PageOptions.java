package com.mindee;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PageOptions {

  public enum PageOptionsOperation {
    KEEP_ONLY_LISTED_PAGES,
    REMOVE_LISTED_PAGES
  }

  private PageOptionsOperation mode;
  private List<Integer> pages;
  @Builder.Default
  private Integer onMinPages = 10;

}
