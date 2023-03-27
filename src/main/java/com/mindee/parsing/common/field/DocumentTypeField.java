package com.mindee.parsing.common.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Represent a string field.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class DocumentTypeField {

  /**
   * The value of the field.
   */
  @JsonProperty("value")
  private String value;

  @Override
  public String toString() {
    return value == null ? "" : value;
  }
}
