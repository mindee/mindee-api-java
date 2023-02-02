package com.mindee.parsing.common;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

/**
 * Represent error details.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ErrorDetailsDeserializer.class)
public class ErrorDetails {

  /**
   * More precise information about the current error.
   */
  private final String value;

  public ErrorDetails(String details) {
    this.value = details;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
