package com.mindee.geometry;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * A relative set of coordinates (X, Y) on the document.
 */
@Value
public class Point {
  Double x;
  Double y;

  @Builder
  @Jacksonized
  public Point(Double x, Double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || this.getClass() != object.getClass()) {
      return false;
    }
    Point point = (Point) object;
    return this.x.equals(point.x) && this.y.equals(point.y);
  }
}
