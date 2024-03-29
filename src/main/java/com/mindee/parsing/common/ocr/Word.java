package com.mindee.parsing.common.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.geometry.Polygon;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents a word.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Word {

  /**
   * The confidence about the zone of the value extracted.
   * A value from 0 to 1.
   */
  @JsonProperty("confidence")
  private double confidence;
  /**
   * Define the coordinates of the zone in the page where the values have been found.
   */
  @JsonProperty("polygon")
  private Polygon polygon;
  /**
   * Represent the content.
   */
  @JsonProperty("text")
  private String text;
}
