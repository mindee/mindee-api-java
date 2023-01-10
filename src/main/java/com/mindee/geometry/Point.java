package com.mindee.geometry;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
public final class Point {
  private Double x;
  private Double y;

  @Builder @Jacksonized
  public Point(Double x, Double y)
  {
    this.x =x;
    this.y =y;
  }

}
