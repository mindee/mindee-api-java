package com.mindee.v2.parsing.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.parsing.CommonResponse;
import java.util.List;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Models search response.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse extends CommonResponse {

  @JsonProperty("models")
  private List<SearchModel> models;

  @JsonProperty("pagination")
  private PaginationMetadata pagination;

  /**
   * String representation of the search response.
   */
  @Override
  public String toString() {
    var joiner = new StringJoiner("\n");
    joiner.add("Models").add("#######");
    for (SearchModel model : models) {
      joiner.add("* :Name: " + model.getName());
      joiner.add("  :ID: " + model.getId());
      joiner.add("  :Model Type: " + model.getModelType());
    }
    joiner.add("Pagination").add("##########");
    joiner.add(pagination.toString());
    return joiner.toString();
  }
}
