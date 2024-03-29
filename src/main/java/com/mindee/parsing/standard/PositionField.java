package com.mindee.parsing.standard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonDeserializer;
import lombok.Getter;

/**
 * A detected element in the image.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionField implements PositionData {

  /**
   * Straight rectangle.
   */
  @JsonProperty("bounding_box")
  @JsonDeserialize(using = PolygonDeserializer.class)
  private Polygon boundingBox;
  /**
   * Free polygon with up to 30 vertices.
   */
  @JsonProperty("polygon")
  @JsonDeserialize(using = PolygonDeserializer.class)
  private Polygon polygon;
  /**
   * Free polygon with 4 vertices.
   */
  @JsonProperty("quadrangle")
  @JsonDeserialize(using = PolygonDeserializer.class)
  private Polygon quadrangle;
  /**
   * Rectangle that may be oriented (can go beyond the canvas).
   */
  @JsonProperty("rectangle")
  @JsonDeserialize(using = PolygonDeserializer.class)
  private Polygon rectangle;

  public String toString() {
    if (polygon != null && !polygon.isEmpty()) {
      return polygon.toString();
    }
    if (boundingBox != null && !boundingBox.isEmpty()) {
      return boundingBox.toString();
    }
    if (quadrangle != null && !quadrangle.isEmpty()) {
      return quadrangle.toString();
    }
    if (rectangle != null && !rectangle.isEmpty()) {
      return rectangle.toString();
    }
    return "";
  }
}
