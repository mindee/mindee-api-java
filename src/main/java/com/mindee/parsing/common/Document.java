package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Define the parsed document.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document<T extends Inference> {

  /**
   * Define the inference model of values.
   */
  @JsonProperty("inference")
  private T inference;
  /**
   * The original file name of the parsed document.
   */
  @JsonProperty("name")
  private String filename;

  @Override
  public String toString() {
    return
      String.format("####################%n") +
        String.format("%s v%s%n", inference.getProduct().getName(), inference.getProduct().getVersion()) +
        String.format("####################%n") +
        String.format(":Filename: %s%n", filename) +
        String.format(":Rotation applied: %s%n%n", inference.isRotationApplied() ? "Yes" : "No") +
        inference.toString();
  }
}
