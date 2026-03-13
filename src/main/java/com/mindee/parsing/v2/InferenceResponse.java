package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.http.ProductInfo;
import lombok.Getter;

/**
 * Response for an extraction inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ProductInfo(slug = "extraction")
public class InferenceResponse extends CommonResponse {

  /**
   * Inference result.
   */
  @JsonProperty("inference")
  private Inference inference;
}
