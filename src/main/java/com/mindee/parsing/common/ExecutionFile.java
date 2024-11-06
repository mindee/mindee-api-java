package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Representation of a workflow execution's file data.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionFile {
  /**
   * File name.
   */
  @JsonProperty("name")
  private String name;

  /**
   * Identifier for the execution.
   */
  @JsonProperty("alias")
  private String alias;
}
