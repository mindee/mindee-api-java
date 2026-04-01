package com.mindee.v2.product.split;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.http.ProductInfo;
import com.mindee.v2.parsing.CommonResponse;
import lombok.Getter;

/**
 * Response for a crop utility inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ProductInfo(slug = "split")
public class SplitResponse extends CommonResponse {

  /**
   * The inference result for a split utility request.
   */
  @JsonProperty("inference")
  private SplitInference inference;
}
