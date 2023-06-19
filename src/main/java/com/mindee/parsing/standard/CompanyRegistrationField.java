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
  @JsonProperty("value")
  private String value;
  /**
   * Type of the company registration number.
   */
  @JsonProperty("type")
  private String type;

  @Override
  public String toString() {
    return value == null ? "" : value;
  }
}
