package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Represents an asynchronous inference response (V2).
 */
@Getter
public class InferenceResponse extends CommonResponse {

  /**
   * Inference result.
   */
  @JsonProperty("inference")
  private Inference inference;
}
