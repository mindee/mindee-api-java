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
  /**
   * The Mindee Id of the current document.
   */
  @JsonProperty("id")
  private String id;
  /**
   * Ocr result information.
   */
  @JsonProperty("ocr")
  private Ocr ocr;

  @Override
  public String toString() {
    return
      String.format("########%n") +
        String.format("Document%n") +
        String.format("########%n") +
        String.format(":Mindee ID: %s%n", id) +
        String.format(":Filename: %s%n", filename) +
        inference.toString();
  }
}
