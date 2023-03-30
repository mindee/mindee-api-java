package com.mindee.geometry;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;

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

    DoubleSummaryStatistics statsX = polygon.getCoordinates().stream()
        .mapToDouble(Point::getX)
        .summaryStatistics();

    DoubleSummaryStatistics statsY = polygon.getCoordinates()
        .stream().mapToDouble(Point::getY)
        .summaryStatistics();

    return new Bbox(
      statsX.getMin(),
      statsX.getMax(),
      statsY.getMin(),
      statsY.getMax()
    );
  }

  /**
   * Generate a BBox from a list of Polygons.
   */
  public static Bbox generate(List<Polygon> polygons) {

    if (polygons.isEmpty()) {
      return null;
    }

    Optional<Polygon> mergedPolygon = polygons.stream()
        .reduce(PolygonUtils::combine);

    if (!mergedPolygon.isPresent()) {
      return null;
    }

    return generate(mergedPolygon.get());
  }

  /**
   * Merge a list of BBoxes.
   */
  public static Bbox merge(List<Bbox> bboxes) {
    if (bboxes.isEmpty()) {
      return null;
    }

    return new Bbox(
      bboxes.stream().map(Bbox::getMinX).min(Double::compare).get(),
      bboxes.stream().map(Bbox::getMaxX).max(Double::compare).get(),
      bboxes.stream().map(Bbox::getMinY).min(Double::compare).get(),
      bboxes.stream().map(Bbox::getMaxY).max(Double::compare).get()
    );
  }

}
