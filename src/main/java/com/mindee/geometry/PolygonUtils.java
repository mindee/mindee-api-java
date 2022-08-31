package com.mindee.geometry;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class PolygonUtils {
  private PolygonUtils() {
  }

  public static Polygon getFrom(List<List<Double>> polygon) {
    List<Point> coordinates = polygon.stream()
        .map(coordinate -> new Point(coordinate.get(0), coordinate.get(1)))
        .collect(Collectors.toList());

    return new Polygon(coordinates);
  }

  /**
   * Get the central coordinates (centroid) given a list of coordinates (aka a
   * polygon).
   * 
   * @param polygon
   * @return
   */
  public static Point getCentroid(Polygon polygon) {
    int verticesSum = polygon.getCoordinates().size();

    double xSum = polygon.getCoordinates().stream().map(Point::getX).mapToDouble(Double::doubleValue)
        .sum();
    double ySum = polygon.getCoordinates().stream().map(Point::getY).mapToDouble(Double::doubleValue)
        .sum();

    return new Point(xSum / verticesSum, ySum / verticesSum);
  }

  public static Double getMinYCoordinate(Polygon polygon) {
    OptionalDouble min = polygon.getCoordinates().stream().map(Point::getY)
        .mapToDouble(Double::doubleValue).min();
    if (min.isPresent()) {
      return min.getAsDouble();
    }
    return null;
  }

  public static Double getMaxYCoordinate(Polygon polygon) {
    OptionalDouble max = polygon.getCoordinates().stream().map(Point::getY)
        .mapToDouble(Double::doubleValue).max();
    if (max.isPresent()) {
      return max.getAsDouble();
    }
    return null;
  }

  public static Double getMinXCoordinate(Polygon polygon) {
    OptionalDouble min = polygon.getCoordinates().stream().map(Point::getX)
        .mapToDouble(Double::doubleValue).min();
    if (min.isPresent()) {
      return min.getAsDouble();
    }
    return null;
  }

  public static Double getMaxXCoordinate(Polygon polygon) {
    OptionalDouble max = polygon.getCoordinates().stream().map(Point::getX)
        .mapToDouble(Double::doubleValue).max();
    if (max.isPresent()) {
      return max.getAsDouble();
    }
    return null;
  }

  /**
   * Merge the coordinates of the two polygons
   * 
   * @param base
   * @param target
   * @return the new merged polygon
   */
  public static Polygon combine(Polygon base, Polygon target) {
    if (base == null) {
      base = new Polygon(Arrays.asList(new Point(0.0, 0.0)));
    }
    if (target == null) {
      target = new Polygon(Arrays.asList(new Point(0.0, 0.0)));
    }

    Double maxx = Math.max(target.getCoordinates().stream()
        .map(Point::getX)
        .max(Double::compareTo).orElse(Double.MIN_VALUE),
        base.getCoordinates().stream()
            .map(Point::getX)
            .max(Double::compareTo).orElse(Double.MIN_VALUE));

    Double minx = Math.min(target.getCoordinates().stream()
        .map(Point::getX)
        .min(Double::compareTo).orElse(Double.MAX_VALUE),
        base.getCoordinates().stream()
            .map(Point::getX)
            .min(Double::compareTo).orElse(Double.MAX_VALUE));

    Double maxy = Math.max(target.getCoordinates().stream()
        .map(Point::getY)
        .max(Double::compareTo).orElse(Double.MIN_VALUE),
        base.getCoordinates().stream()
            .map(Point::getY)
            .max(Double::compareTo).orElse(Double.MIN_VALUE));

    Double miny = Math.min(target.getCoordinates().stream()
        .map(Point::getY)
        .min(Double::compareTo).orElse(Double.MAX_VALUE),
        base.getCoordinates().stream()
            .map(Point::getY)
            .min(Double::compareTo).orElse(Double.MAX_VALUE));

    return new Polygon(
        Arrays.asList(
            new Point(minx, miny),
            new Point(maxx, miny),
            new Point(maxx, maxy),
            new Point(minx, maxy)));
  }
}
