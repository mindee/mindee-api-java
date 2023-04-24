package com.mindee.geometry;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * Contains any number of vertex coordinates (Points).
 */
@Getter
@JsonDeserialize(using = PolygonDeserializer.class)
public class Polygon {
  private List<Point> coordinates = new ArrayList<>();

  @Builder
  public Polygon(List<Point> coordinates) {
    this.coordinates = coordinates;
  }
}
