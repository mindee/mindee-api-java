package com.mindee.v2.search.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.parsing.search.BaseSearchResponse;
import com.mindee.v2.parsing.search.SearchModels;
import com.mindee.v2.product.ProductAttributes;
import java.util.List;
import lombok.Getter;

/**
 * Models search response.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ProductAttributes(slug = "models")
public class ModelSearchResponse extends BaseSearchResponse {

  /**
   * Paginated list of matching models.
   */
  @JsonProperty("models")
  private SearchModels models;

  @Override
  protected List<String> bodyLines() {
    return List.of("Models\n######\n", String.valueOf(models));
  }
}
