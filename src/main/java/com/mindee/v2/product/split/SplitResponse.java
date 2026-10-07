package com.mindee.v2.product.split;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.parsing.BaseResponse;
import com.mindee.v2.product.ProductAttributes;
import lombok.Getter;

/**
 * Response for a crop utility inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ProductAttributes(slug = "split")
public class SplitResponse extends BaseResponse {

  /**
   * The inference result for a split utility request.
   */
  @JsonProperty("inference")
  private SplitInference inference;
}
