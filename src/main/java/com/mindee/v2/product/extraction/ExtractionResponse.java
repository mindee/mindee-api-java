package com.mindee.v2.product.extraction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.http.ProductInfo;
import com.mindee.v2.parsing.CommonResponse;
import lombok.Getter;

/**
 * Response for an extraction inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ProductInfo(slug = "extraction")
public class ExtractionResponse extends CommonResponse {

  /**
   * Inference result.
   */
  @JsonProperty("inference")
  private ExtractionInference inference;
}
