package com.mindee.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public class Polygon {
  private List<Point> coordinates = new ArrayList<>();
  private BoundingBox bbox;

  /**
   * @param points
   */
  public Polygon(List<Point> coordinates) {
    this.coordinates = coordinates;
    this.bbox = BoundingBoxUtils.createFrom(this.coordinates);
  }

  /**
   * @return a polygon as a boundingBox
   */
  public Polygon getBboxAsPolygon() {
    return new Polygon(Arrays.asList(
        new Point(this.bbox.getMinX(),
            this.bbox.getMinY()),
        new Point(this.bbox.getMaxX(),
            this.bbox.getMinY()),
        new Point(this.bbox.getMaxX(),
            this.bbox.getMaxY()),
        new Point(this.bbox.getMinX(),
            this.bbox.getMaxY())));
  }

}