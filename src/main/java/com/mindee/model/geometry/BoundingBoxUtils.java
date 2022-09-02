package com.mindee.model.geometry;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

public final class BoundingBoxUtils {
  private BoundingBoxUtils() {
  }

  public static Polygon createFrom(Polygon polygon) {
    DoubleSummaryStatistics xStatistics = polygon.getCoordinates().stream()
        .mapToDouble(Point::getX)
        .summaryStatistics();

    DoubleSummaryStatistics yStatistics = polygon.getCoordinates()
        .stream().mapToDouble(Point::getY)
        .summaryStatistics();

    return new Polygon(Arrays.asList(
        new Point(xStatistics.getMin(), yStatistics.getMin()),
        new Point(xStatistics.getMax(), yStatistics.getMin()),
        new Point(xStatistics.getMax(), yStatistics.getMax()),
        new Point(xStatistics.getMin(), yStatistics.getMax())));
  }
}
