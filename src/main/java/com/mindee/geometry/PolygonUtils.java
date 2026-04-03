package com.mindee.geometry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Methods for working with Polygons.
 * Used internally, use at your own risk.
 */
public final class PolygonUtils {
  private PolygonUtils() {
  }

  /**
   * Get the central coordinates (centroid) of a list of Points.
   */
  public static Point getCentroid(List<Point> vertices) {
    int verticesSum = vertices.size();

    double xSum = vertices.stream().map(Point::getX).mapToDouble(Double::doubleValue).sum();
    double ySum = vertices.stream().map(Point::getY).mapToDouble(Double::doubleValue).sum();
    return new Point(xSum / verticesSum, ySum / verticesSum);
  }

  /**
   * Get the maximum and minimum Y coordinates in a given list of Points.
   */
  public static MinMax getMinMaxY(List<Point> vertices) {
    List<Double> points = vertices.stream().map(Point::getY).collect(Collectors.toList());
    return new MinMax(Collections.min(points), Collections.max(points));
  }

  /**
   * Get the maximum and minimum X coordinates in a given list of Points.
   */
  public static MinMax getMinMaxX(List<Point> vertices) {
    List<Double> points = vertices.stream().map(Point::getX).collect(Collectors.toList());
    return new MinMax(Collections.min(points), Collections.max(points));
  }

  /**
   * Determine if a Point is within two Y coordinates.
   */
  public static boolean isPointInY(Point centroid, Double yMin, Double yMax) {
    return yMin <= centroid.getY() && centroid.getY() <= yMax;
  }

  /**
   * Determine if a Point is within a Polygon's Y axis.
   */
  public static boolean isPointInPolygonY(Point centroid, Polygon polygon) {
    MinMax yCoords = getMinMaxY(polygon.getCoordinates());
    return isPointInY(centroid, yCoords.getMin(), yCoords.getMax());
  }

  /**
   * Merge the coordinates of the two polygons.
   */
  public static Polygon combine(Polygon base, Polygon target) {
    if (base == null && target == null) {
      throw new IllegalStateException("Previous and next polygons can not be null.");
    }

    if (base == null) {
      base = target;
    }
    if (target == null) {
      target = base;
    }

    Double maxx = Math
      .max(
        target
          .getCoordinates()
          .stream()
          .map(Point::getX)
          .max(Double::compareTo)
          .orElse(Double.MIN_VALUE),
        base
          .getCoordinates()
          .stream()
          .map(Point::getX)
          .max(Double::compareTo)
          .orElse(Double.MIN_VALUE)
      );

    Double minx = Math
      .min(
        target
          .getCoordinates()
          .stream()
          .map(Point::getX)
          .min(Double::compareTo)
          .orElse(Double.MAX_VALUE),
        base
          .getCoordinates()
          .stream()
          .map(Point::getX)
          .min(Double::compareTo)
          .orElse(Double.MAX_VALUE)
      );

    Double maxy = Math
      .max(
        target
          .getCoordinates()
          .stream()
          .map(Point::getY)
          .max(Double::compareTo)
          .orElse(Double.MIN_VALUE),
        base
          .getCoordinates()
          .stream()
          .map(Point::getY)
          .max(Double::compareTo)
          .orElse(Double.MIN_VALUE)
      );

    Double miny = Math
      .min(
        target
          .getCoordinates()
          .stream()
          .map(Point::getY)
          .min(Double::compareTo)
          .orElse(Double.MAX_VALUE),
        base
          .getCoordinates()
          .stream()
          .map(Point::getY)
          .min(Double::compareTo)
          .orElse(Double.MAX_VALUE)
      );

    return new Polygon(
      Arrays
        .asList(
          new Point(minx, miny),
          new Point(maxx, miny),
          new Point(maxx, maxy),
          new Point(minx, maxy)
        )
    );
  }
}
