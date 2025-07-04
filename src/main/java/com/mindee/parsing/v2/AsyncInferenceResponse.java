package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AsyncInferenceResponse extends CommonResponse {

  /**
   * Inference result.
   */
  @JsonProperty("inference")
  private Inference inference;
}
