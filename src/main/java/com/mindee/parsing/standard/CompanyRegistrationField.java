package com.mindee.parsing.standard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Represent a company registration.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CompanyRegistrationField extends BaseField {

  /**
   * The value of the field.
   */
  private final String value;

  /**
   * Type of the company registration number.
   */
  private final String type;

  public CompanyRegistrationField(
      @JsonProperty("type")
      String type,
      @JsonProperty("value")
      String value
  ) {
    this.type = type;
    this.value = value;
  }

  public boolean isEmpty() {
    return (
        (value == null || value.isEmpty())
        && (type == null || type.isEmpty())
      );
  }

  @Override
  public String toString() {
    return value == null ? "" : value;
  }
}
