package com.mindee.geometry;

import java.util.List;

/**
 * Methods for working with BBoxes.
 */
public final class BboxUtils {
  private BboxUtils() {
  }

  /**
   * Generate a BBox from a single Polygon.
   */
  public static Bbox generate(Polygon polygon) {

    if (polygon == null) {
      return null;
    }

    var statsX = polygon.getCoordinates().stream().mapToDouble(Point::getX).summaryStatistics();
    var statsY = polygon.getCoordinates().stream().mapToDouble(Point::getY).summaryStatistics();

    return new Bbox(statsX.getMin(), statsX.getMax(), statsY.getMin(), statsY.getMax());
  }

  /**
   * Generate a BBox from a list of Polygons.
   */
  public static Bbox generate(List<Polygon> polygons) {

    if (polygons.isEmpty()) {
      return null;
    }

    return polygons.stream().reduce(PolygonUtils::combine).map(BboxUtils::generate).orElse(null);
  }

  /**
   * Merge a list of BBoxes.
   */
  public static Bbox merge(List<Bbox> bboxes) {
    if (bboxes.isEmpty()) {
      return null;
    }

    return new Bbox(
      bboxes.stream().mapToDouble(Bbox::getMinX).min().getAsDouble(),
      bboxes.stream().mapToDouble(Bbox::getMaxX).max().getAsDouble(),
      bboxes.stream().mapToDouble(Bbox::getMinY).min().getAsDouble(),
      bboxes.stream().mapToDouble(Bbox::getMaxY).max().getAsDouble()
    );
  }

}
