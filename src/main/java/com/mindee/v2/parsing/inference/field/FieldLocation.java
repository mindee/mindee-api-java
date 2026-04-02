package com.mindee.v2.parsing.inference.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonDeserializer;
import com.mindee.geometry.PositionDataField;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Location data for a field.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class FieldLocation implements PositionDataField {

  /**
   * Free polygon made up of points.
   */
  @JsonProperty("polygon")
  @JsonDeserialize(using = PolygonDeserializer.class)
  private Polygon polygon;

  /**
   * 0-based page index of where the polygon is located.
   */
  @JsonProperty("page")
  private int page;

  @Override
  public String toString() {
    return polygon.toStringPrecise() + " on page " + page;
  }
}
