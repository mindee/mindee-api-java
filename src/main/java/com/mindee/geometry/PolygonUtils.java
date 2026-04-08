package com.mindee.geometry;

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

    double xSum = vertices.stream().mapToDouble(Point::getX).sum();
    double ySum = vertices.stream().mapToDouble(Point::getY).sum();
    return new Point(xSum / verticesSum, ySum / verticesSum);
  }

  /**
   * Get the maximum and minimum Y coordinates in a given list of Points.
   */
  public static MinMax getMinMaxY(List<Point> vertices) {
    var stats = vertices.stream().mapToDouble(Point::getY).summaryStatistics();
    return new MinMax(stats.getMin(), stats.getMax());
  }

  /**
   * Get the maximum and minimum X coordinates in a given list of Points.
   */
  public static MinMax getMinMaxX(List<Point> vertices) {
    var stats = vertices.stream().mapToDouble(Point::getX).summaryStatistics();
    return new MinMax(stats.getMin(), stats.getMax());
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

    var combinedCoords = java.util.stream.Stream
      .concat(base.getCoordinates().stream(), target.getCoordinates().stream())
      .collect(Collectors.toList());

    var xStats = combinedCoords.stream().mapToDouble(Point::getX).summaryStatistics();
    var yStats = combinedCoords.stream().mapToDouble(Point::getY).summaryStatistics();

    return new Polygon(
      List
        .of(
          new Point(xStats.getMin(), yStats.getMin()),
          new Point(xStats.getMax(), yStats.getMin()),
          new Point(xStats.getMax(), yStats.getMax()),
          new Point(xStats.getMin(), yStats.getMax())
        )
    );
  }
}
