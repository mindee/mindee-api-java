package com.mindee.geometry;

import lombok.Getter;

/**
 * True representation of a mathematical bounding box.
 */
@Getter
public final class Bbox {

  private final double minX;
  private final double minY;
  private final double maxX;
  private final double maxY;

  /**
   * @param xMin
   * @param xMax
   * @param yMin
   * @param yMax
   */
  public Bbox(double xMin, double xMax, double yMin, double yMax) {
    this.minX = xMin;
    this.minY = yMin;
    this.maxX = xMax;
    this.maxY = yMax;
  }
}
