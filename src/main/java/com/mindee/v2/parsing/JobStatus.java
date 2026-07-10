package com.mindee.v2.parsing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Possible statuses returned by the API for an asynchronous {@link Job}.
 */
public enum JobStatus {
  Processing("Processing"),
  Processed("Processed"),
  Failed("Failed"),
  Unknown("Unknown");

  private final String value;

  JobStatus(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  /**
   * Deserializes a status string coming from the API. Unknown values fall back to
   * {@link #Unknown} rather than failing so newly added statuses don't break clients.
   */
  @JsonCreator
  public static JobStatus fromValue(String value) {
    if (value == null) {
      return null;
    }
    for (JobStatus status : values()) {
      if (status.value.equalsIgnoreCase(value)) {
        return status;
      }
    }
    return Unknown;
  }
}
