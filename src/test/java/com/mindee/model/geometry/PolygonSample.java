package com.mindee.model.geometry;

import java.util.Arrays;
import java.util.List;

public final class PolygonSample {
  private PolygonSample() {
  }

  public static List<Point> getPointsAsRectangle() {
    return Arrays.asList(
        new Point(0.123, 0.53),
        new Point(0.175, 0.53),
        new Point(0.175, 0.546),
        new Point(0.123, 0.546));
  }

  public static List<Point> getPointsWichIsNotRectangle() {
    return Arrays.asList(
        new Point(0.205, 0.407),
        new Point(0.379, 0.407),
        new Point(0.381, 0.43),
        new Point(0.207, 0.43));
  }

  public static Polygon getPolygonAsRectangle() {
    return new Polygon(getPointsAsRectangle());
  }

  public static Polygon getPolygonWichIsNotRectangle() {
    return new Polygon(getPointsWichIsNotRectangle());
  }
}
