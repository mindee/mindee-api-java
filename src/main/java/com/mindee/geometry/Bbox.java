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
   * The default constructor.
   *
   * @param minX The minimum X coordinate.
   *
   * @param maxX The maximum X coordinate.
   *
   * @param minY The minimal Y coordinate.
   *
   * @param maxY The maximum Y coordinate.
   */
  public Bbox(double minX, double maxX, double minY, double maxY) {
    this.minX = minX;
    this.minY = minY;
    this.maxX = maxX;
    this.maxY = maxY;
  }
}
