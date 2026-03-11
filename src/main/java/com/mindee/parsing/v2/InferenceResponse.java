package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Response for an extraction inference.
 */
@Getter
public class InferenceResponse extends CommonResponse {

  /**
   * Inference result.
   */
  @JsonProperty("inference")
  private Inference inference;
}
