package com.mindee.geometry;

import java.util.DoubleSummaryStatistics;

public final class BoundingBoxUtils {
  private BoundingBoxUtils() {
  }

  public static BoundingBox createFrom(Polygon polygon) {
    DoubleSummaryStatistics xStatistics = polygon.getCoordinates().stream()
        .mapToDouble(point -> point.getX())
        .summaryStatistics();

    DoubleSummaryStatistics yStatistics = polygon.getCoordinates()
        .stream().mapToDouble(point -> point.getY())
        .summaryStatistics();

    return new BoundingBox(xStatistics.getMin(), xStatistics.getMax(), yStatistics.getMin(), yStatistics.getMax());
  }
}
