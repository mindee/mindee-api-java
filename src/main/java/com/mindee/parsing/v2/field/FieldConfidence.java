package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Confidence level of a field as returned by the V2 API.
 */
public enum FieldConfidence {
  Low("Low"),
  Medium("Medium"),
  High("High"),
  Certain("Certain");

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

  /**
   * Compares the current FieldConfidence level with another FieldConfidence level
   * to determine if the current level is greater.
   *
   * @param other the other FieldConfidence level to compare against
   * @return true if the current FieldConfidence level is greater than the specified level,
   * false otherwise
   */
  public boolean greaterThan(FieldConfidence other) {
    return this.compareTo(other) > 0;
  }

  /**
   * Compares the current FieldConfidence level with another FieldConfidence level
   * to determine if the current level is greater than or equal to the specified level.
   *
   * @param other the other FieldConfidence level to compare against
   * @return true if the current FieldConfidence level is greater than or equal to the specified
   * level, false otherwise
   */
  public boolean greaterThanOrEqual(FieldConfidence other) {
    return this.compareTo(other) >= 0;
  }

  /**
   * Compares the current FieldConfidence level with another FieldConfidence level
   * to determine if the current level is less than the specified level.
   *
   * @param other the other FieldConfidence level to compare against
   * @return true if the current FieldConfidence level is less than the specified level,
   * false otherwise
   */
  public boolean lessThan(FieldConfidence other) {
    return this.compareTo(other) < 0;
  }

  /**
   * Compares the current FieldConfidence level with another FieldConfidence level
   * to determine if the current level is less than or equal to the specified level.
   *
   * @param other the other FieldConfidence level to compare against
   * @return true if the current FieldConfidence level is less than or equal to the specified level,
   * false otherwise
   */
  public boolean lessThanOrEqual(FieldConfidence other) {
    return this.compareTo(other) <= 0;
  }
}
