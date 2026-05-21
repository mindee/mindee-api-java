package com.mindee.v2.parsing.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PaginationMetadata data associated with model search.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class PaginationMetadata {

  /**
   * Number of items per page.
   */
  @JsonProperty("per_page")
  private int perPage;

  /**
   * 1-indexed page number.
   */
  @JsonProperty("page")
  private int page;

  /**
   * Total items.
   */
  @JsonProperty("total_items")
  private int totalItems;

  /**
   * Total number of pages.
   */
  @JsonProperty("total_pages")
  private int totalPages;

  /**
   * String representation of the pagination metadata.
   */
  @Override
  public String toString() {
    var joiner = new StringJoiner("\n");
    joiner.add(":Per Page: " + perPage);
    joiner.add(":Page: " + page);
    joiner.add(":Total Items: " + totalItems);
    joiner.add(":Total Pages: " + totalPages);
    return joiner.toString();
  }
}
