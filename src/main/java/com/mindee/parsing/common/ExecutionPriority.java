package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Execution priority for a workflow.
 */
public enum ExecutionPriority {
  LOW("low"),
  MEDIUM("medium"),
  HIGH("high");

  private final String value;

  ExecutionPriority(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
