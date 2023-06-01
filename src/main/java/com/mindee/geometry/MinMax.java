package com.mindee.geometry;
import lombok.Getter;

/** Minimum and maximum values. */
@Getter
public class MinMax {
  private final Double min;
  private final Double max;

  public MinMax(Double min, Double max) {
    this.min = min;
    this.max = max;
  }
}
