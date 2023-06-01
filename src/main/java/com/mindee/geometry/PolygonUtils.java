package com.mindee.geometry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * Methods for working with Polygons.
 */
public final class PolygonUtils {
  private PolygonUtils() {
  }

  public static Polygon getFrom(List<List<Double>> polygon) {
    List<Point> coordinates = polygon.stream()
        .map(coordinate -> new Point(coordinate.get(0), coordinate.get(1)))
        .collect(Collectors.toList());

    return new Polygon(coordinates);
  }

  /**
   * Get the central coordinates (centroid) given a list of coordinates
   * (AKA a polygon).
   */
  public static Point getCentroid(Polygon polygon) {
    int verticesSum = polygon.getCoordinates().size();

    double xSum = polygon.getCoordinates().stream()
        .map(Point::getX)
        .mapToDouble(Double::doubleValue).sum();
    double ySum = polygon.getCoordinates().stream()
        .map(Point::getY)
        .mapToDouble(Double::doubleValue).sum();

    return new Point(xSum / verticesSum, ySum / verticesSum);
  }

  /**
   * Compare two polygons based on their Y coordinates.
   * Useful for sorting lists.
   */
  public static int CompareOnY(Polygon polygon1, Polygon polygon2) {
    double sort = getMinYCoordinate(polygon1) - getMinYCoordinate(polygon2);
    if (sort == 0) {
      return 0;
    }
    return sort < 0 ? -1 : 1;
  }

  /**
   * Compare two polygons based on their X coordinates.
   * Useful for sorting lists.
   */
  public static int CompareOnX(Polygon polygon1, Polygon polygon2) {
    double sort = getMinXCoordinate(polygon1) - getMinXCoordinate(polygon2);
    if (sort == 0) {
      return 0;
    }
    return sort < 0 ? -1 : 1;
  }

  /**
   * Get the maximum and minimum Y coordinates in a given list of Points.
   */
  public static MinMax getMinMaxY(List<Point> vertices) {
    List<Double> points = vertices.stream().map(Point::getY).collect(Collectors.toList());
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

  public static Double getMinYCoordinate(Polygon polygon) {
    OptionalDouble min = polygon.getCoordinates().stream()
        .map(Point::getY)
        .mapToDouble(Double::doubleValue).min();

    if (min.isPresent()) {
      return min.getAsDouble();
    }

    throw new IllegalStateException(
        "The min Y could not be found "
        + "because it seems that there is no coordinates in the current polygon."
    );
  }

  public static Double getMaxYCoordinate(Polygon polygon) {
    OptionalDouble max = polygon.getCoordinates().stream()
        .map(Point::getY)
        .mapToDouble(Double::doubleValue).max();
    if (max.isPresent()) {
      return max.getAsDouble();
    }

    throw new IllegalStateException(
        "The max Y could not be found "
        + "because it seems that there is no coordinates in the current polygon."
    );
  }

  public static Double getMinXCoordinate(Polygon polygon) {
    OptionalDouble min = polygon.getCoordinates().stream()
        .map(Point::getX)
        .mapToDouble(Double::doubleValue).min();
    if (min.isPresent()) {
      return min.getAsDouble();
    }

    throw new IllegalStateException(
        "The min X could not be found "
        + "because it seems that there is no coordinates in the current polygon."
    );
  }

  public static Double getMaxXCoordinate(Polygon polygon) {
    OptionalDouble max = polygon.getCoordinates().stream()
        .map(Point::getX)
        .mapToDouble(Double::doubleValue).max();
    if (max.isPresent()) {
      return max.getAsDouble();
    }

    throw new IllegalStateException(
        "The max X could not be found "
        + "because it seems that there is no coordinates in the current polygon."
    );
  }

  /**
   * Merge the coordinates of the two polygons
   *
   * @param base
   * @param target
   * @return the new merged polygon
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

    Double maxx = Math.max(
        target.getCoordinates().stream()
          .map(Point::getX)
          .max(Double::compareTo).orElse(Double.MIN_VALUE),
        base.getCoordinates().stream()
          .map(Point::getX)
          .max(Double::compareTo).orElse(Double.MIN_VALUE));

    Double minx = Math.min(
        target.getCoordinates().stream()
          .map(Point::getX)
          .min(Double::compareTo).orElse(Double.MAX_VALUE),
        base.getCoordinates().stream()
          .map(Point::getX)
          .min(Double::compareTo).orElse(Double.MAX_VALUE));

    Double maxy = Math.max(
        target.getCoordinates().stream()
          .map(Point::getY)
          .max(Double::compareTo).orElse(Double.MIN_VALUE),
        base.getCoordinates().stream()
          .map(Point::getY)
          .max(Double::compareTo).orElse(Double.MIN_VALUE)
    );

    Double miny = Math.min(
        target.getCoordinates().stream()
          .map(Point::getY)
          .min(Double::compareTo).orElse(Double.MAX_VALUE),
        base.getCoordinates().stream()
          .map(Point::getY)
          .min(Double::compareTo).orElse(Double.MAX_VALUE)
    );

    return new Polygon(
      Arrays.asList(
        new Point(minx, miny),
        new Point(maxx, miny),
        new Point(maxx, maxy),
        new Point(minx, maxy)
      )
    );
  }
}
