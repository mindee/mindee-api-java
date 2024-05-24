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

  public ClassificationField(@JsonProperty("value") String value) {
    this.value = value;
  }

  public boolean isEmpty() {
    return this.value == null;
  }

  @Override
  public String toString() {
    return value == null ? "" : value;
  }
}
