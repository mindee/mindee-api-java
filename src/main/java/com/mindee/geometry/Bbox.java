package com.mindee.geometry;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * True representation of a mathematical bounding box.
 */
@Getter
@AllArgsConstructor
public final class Bbox {

  /**
   * minX The minimum X coordinate.
   */
  private final double minX;
  /**
   * The maximum X coordinate.
   */
  private final double maxX;
  /**
   * The minimum Y coordinate.
   */
  private final double minY;

  /**
   * The maximum Y coordinate.
   */
  private final double maxY;
  
}
