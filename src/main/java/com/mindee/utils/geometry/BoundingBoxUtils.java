package com.mindee.utils.geometry;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

import com.mindee.model.geometry.Point;
import com.mindee.model.geometry.Polygon;

public final class BoundingBoxUtils {
  private BoundingBoxUtils() {
  }

  public static Polygon createBoundingBoxFrom(Polygon polygon) {
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
