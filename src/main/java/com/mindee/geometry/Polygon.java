package com.mindee.geometry;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
  private List<Point> coordinates = new ArrayList<>();

  /**
   * @param points
   */
  public Polygon(List<Point> coordinates) {
    this.coordinates = coordinates;
  }

  /**
   * @param points
   */
  public Polygon(Point coordinate) {
    this.addCoordinate(coordinate);
  }

  /**
   * @return the coordinates
   */
  public List<Point> getCoordinates() {
    return coordinates;
  }

  public void addCoordinate(Point coordinate) {
    this.coordinates.add(coordinate);
  }

  /**
   * @return the boundingBox
   */
  public BoundingBox getBoundingBox() {
    return BoundingBoxUtils.createFrom(this);
  }

}