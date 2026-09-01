package com.mindee.v2.product.extraction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.product.ProductAttributes;
import lombok.Getter;

/**
 * Response for an extraction inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ProductAttributes(slug = "extraction")
public class ExtractionResponse extends CommonResponse {

  /**
   * Inference result.
   */
  @JsonProperty("inference")
  private ExtractionInference inference;
}
