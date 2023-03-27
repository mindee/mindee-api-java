package com.mindee.geometry;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;

public final class BboxUtils {
  private BboxUtils() {
  }

  public static Bbox generate(Polygon polygon) {

    if (polygon == null) {
      return null;
    }

    DoubleSummaryStatistics xStatistics = polygon.getCoordinates().stream()
      .mapToDouble(Point::getX)
      .summaryStatistics();

    DoubleSummaryStatistics yStatistics = polygon.getCoordinates()
      .stream().mapToDouble(Point::getY)
      .summaryStatistics();

    return new Bbox(xStatistics.getMin(), xStatistics.getMax(), yStatistics.getMin(), yStatistics.getMax());
  }

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

  public static Bbox merge(List<Bbox> bboxs) {
    if (bboxs.isEmpty()) {
      return null;
    }

    return new Bbox(
      bboxs.stream().map(Bbox::getMinX).min(Double::compare)
        .get(),
      bboxs.stream().map(Bbox::getMaxX).max(Double::compare)
        .get(),
      bboxs.stream().map(Bbox::getMinY).min(Double::compare)
        .get(),
      bboxs.stream().map(Bbox::getMaxY).max(Double::compare)
        .get());
  }

}
