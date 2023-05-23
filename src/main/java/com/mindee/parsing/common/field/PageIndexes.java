package com.mindee.parsing.common.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * Represents a grouping of pages
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageIndexes {

  /**
   * The confidence about the zone of the value extracted. A value from 0 to 1.
   */
  @JsonProperty("confidence")
  private Double confidence;

  /**
   * The page indexes in the document that are grouped together
   */
  @JsonProperty("page_indexes")
  private List<Integer> pageIndexes;

  @Override
  public String toString() {
    return "Page Indexes ".concat(
        pageIndexes.stream().map((index) -> index.toString()).collect(Collectors.joining("-")));
  }

}
