package com.mindee.v2.parsing.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.StringJoiner;
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
    var joiner = new StringJoiner("\n");

    joiner.add("Error Details");
    joiner.add("=============");

    joiner.add(":HTTP Status: " + status);
    joiner.add(":Title: " + title);
    joiner.add(":Code: " + code);
    joiner.add(":Detail: " + detail);

    if (errors != null && !errors.isEmpty()) {
      joiner.add("");
      joiner.add("Error Items");
      joiner.add("-----------");

      for (int i = 0; i < errors.size(); i++) {
        var error = errors.get(i);
        joiner.add("**Error " + (i + 1) + ":**");
        joiner.add("  :Pointer: " + error.getPointer());
        joiner.add("  :Detail: " + error.getDetail());

        if (i < errors.size() - 1) {
          joiner.add("");
        }
      }
    }

    return joiner.toString();
  }
}
