package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Error response detailing a problem. The format adheres to RFC 9457.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class ErrorResponse {
  /**
   * A short, human-readable summary of the problem.
   */
  @JsonProperty("title")
  private String title;

  /**
   * A human-readable explanation specific to the occurrence of the problem.
   */
  @JsonProperty("detail")
  private String detail;

  /**
   * The HTTP status code returned by the server.
   */
  @JsonProperty("status")
  private int status;

  /**
   * A machine-readable code specific to the occurrence of the problem.
   */
  @JsonProperty("code")
  private String code;

  /**
   * The HTTP status code returned by the server.
   */
  @JsonProperty("errors")
  private List<ErrorItem> errors;

  /** For prettier display. */
  @Override
  public String toString() {
    return "HTTP " + status + " - " + title + " :: " + code + " - " + detail;
  }
}
