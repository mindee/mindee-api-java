package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an asynchronous inference response (V2).
 */
public class AsyncInferenceResponse extends CommonResponse {

  /**
   * Inference result.
   */
  @JsonProperty("inference")
  private Inference inference;
}
