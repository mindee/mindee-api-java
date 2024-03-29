package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Error information from an API response.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {

  /**
   * Details about it.
   */
  @JsonProperty("details")
  private ErrorDetails details;
  /**
   * More precise information about the current error.
   */
  @JsonProperty("message")
  private String message;
  /**
   * A code to identify it.
   */
  @JsonProperty("code")
  private String code;

  @Override
  public String toString() {
    if (this.code == null) {
      return "";
    }
    return this.code + " : " + this.message + " - " + this.details;
  }
}
