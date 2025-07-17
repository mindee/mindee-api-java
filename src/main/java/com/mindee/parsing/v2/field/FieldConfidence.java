package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Confidence level of a field as returned by the V2 API.
 */
public enum FieldConfidence {
  Certain("Certain"),
  High("High"),
  Medium("Medium"),
  Low("Low");

  private final String json;

  FieldConfidence(String json) {
    this.json = json;
  }

  @JsonValue
  public String toJson() {
    return json;
  }

  @JsonCreator
  public static FieldConfidence fromJson(String value) {
    if (value == null) {
      return null;
    }
    for (FieldConfidence level : values()) {
      if (level.json.equalsIgnoreCase(value)) {
        return level;
      }
    }
    throw new IllegalArgumentException("Unknown confidence level '" + value + "'.");
  }
}
