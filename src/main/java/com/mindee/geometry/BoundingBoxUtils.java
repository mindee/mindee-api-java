package com.mindee.geometry;

import java.util.DoubleSummaryStatistics;
import java.util.List;

public final class BoundingBoxUtils {
  private BoundingBoxUtils() {
  }

  public static BoundingBox createFrom(List<Point> points) {
    DoubleSummaryStatistics xStatistics = points.stream()
        .mapToDouble(Point::getX)
        .summaryStatistics();

    DoubleSummaryStatistics yStatistics = points
        .stream().mapToDouble(Point::getY)
        .summaryStatistics();

    return new BoundingBox(xStatistics.getMin(), xStatistics.getMax(), yStatistics.getMin(), yStatistics.getMax());
  }

  public static BoundingBox createFrom(Polygon polygon) {
    DoubleSummaryStatistics xStatistics = polygon.getCoordinates().stream()
        .mapToDouble(Point::getX)
        .summaryStatistics();

    DoubleSummaryStatistics yStatistics = polygon.getCoordinates()
        .stream().mapToDouble(Point::getY)
        .summaryStatistics();

    return new BoundingBox(xStatistics.getMin(), xStatistics.getMax(), yStatistics.getMin(), yStatistics.getMax());
  }
}
