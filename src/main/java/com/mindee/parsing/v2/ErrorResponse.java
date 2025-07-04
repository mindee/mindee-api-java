package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Error information from the API.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class ErrorResponse {
  /**
   * Detail relevant to the error.
   */
  @JsonProperty("detail")
  private String detail;

  /**
   * HTTP error code.
   */
  @JsonProperty("status")
  private int status;

  /** For prettier display. */
  @Override
  public String toString() {
    return "HTTP Status: " + status + " - " + detail;
  }
}
