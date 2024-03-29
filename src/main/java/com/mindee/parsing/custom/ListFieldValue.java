package com.mindee.parsing.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * A single value in a ListField.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListFieldValue {

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
  @JsonDeserialize(using = PolygonDeserializer.class)
  private Polygon polygon;
  /**
   * The content of the value.
   */
  @JsonProperty("content")
  private String content;
  /**
   * The document page on which the information was found.
   */
  @JsonProperty("page_id")
  private Integer pageId;

  public ListFieldValue() {
  }

  public ListFieldValue(
      String content,
      double confidence,
      Polygon polygon
  ) {
    this.confidence = confidence;
    this.polygon = polygon;
    this.content = content;
  }

  @Override
  public String toString() {
    return content == null ? "" : content;
  }
}
