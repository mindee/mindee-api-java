package com.mindee.parsing.standard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Represents a classifier value.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ClassificationField {

  /**
   * The value as a String.
   */
  @JsonProperty("value")
  private String value;

  @Override
  public String toString() {
    return value == null ? "" : value;
  }
}
