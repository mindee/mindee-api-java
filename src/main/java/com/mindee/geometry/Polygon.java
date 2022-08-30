package com.mindee.geometry;

import java.util.ArrayList;
import java.util.List;

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
   * @return the coordinates
   */
  public List<Point> getCoordinates() {
    return coordinates;
  }

  /**
   * @return the boundingBox
   */
  public BoundingBox getBbox() {
    return this.bbox;
  }

  /**
   * @return a polygon as a boundingBox
   */
  public Polygon getBboxAsPolygon() {
    return new Polygon(List.of(
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