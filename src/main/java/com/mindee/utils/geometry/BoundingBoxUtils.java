package com.mindee.utils.geometry;

import com.mindee.model.geometry.Point;
import com.mindee.model.geometry.Polygon;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

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
