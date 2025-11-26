package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;

/**
 * Data schema options activated during the inference.
 */
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSchemaActiveOptions {

  @JsonProperty("override")
  private boolean override;

  /**
   * Whether a data schema override was provided for the inference.
   */
  public boolean getOverride() {
    return override;
  }
}
