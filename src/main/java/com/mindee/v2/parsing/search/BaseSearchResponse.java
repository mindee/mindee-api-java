package com.mindee.v2.parsing.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.parsing.CommonResponse;
import java.util.List;
import java.util.StringJoiner;
import lombok.Getter;

/**
 * Base class for search responses.
 */
@Getter
public abstract class BaseSearchResponse extends CommonResponse {

  /**
   * Pagination metadata.
   */
  @JsonProperty("pagination")
  protected PaginationMetadata pagination;

  /**
   * String representation of the search response.
   *
   * @return cleaned string summary
   */
  public String toString() {
    var joiner = new StringJoiner("\n");
    bodyLines().forEach(joiner::add);
    joiner.add("Pagination Metadata");
    joiner.add("###################");
    joiner.add(String.valueOf(pagination));
    joiner.add("");
    return joiner.toString();
  }

  /**
   * Lines composing the response-specific body (header + items).
   *
   * @return A list of body lines.
   */
  protected abstract List<String> bodyLines();
}
