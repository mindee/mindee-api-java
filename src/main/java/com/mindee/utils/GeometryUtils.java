package com.mindee.utils;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public final class GeometryUtils {

  private GeometryUtils() {
  }

  public static List<List<Double>> deriveBBoxFromPolygon(List<List<Double>> polygon) {
    DoubleSummaryStatistics xStatistics = polygon.stream().mapToDouble(x -> x.get(0))
        .summaryStatistics();
    DoubleSummaryStatistics yStatistics = polygon.stream().mapToDouble(x -> x.get(1))
        .summaryStatistics();

    return Arrays.asList(Arrays.asList(xStatistics.getMin(), yStatistics.getMax()),
        Arrays.asList(xStatistics.getMax(), yStatistics.getMax()),
        Arrays.asList(xStatistics.getMax(), yStatistics.getMin()),
        Arrays.asList(xStatistics.getMin(), yStatistics.getMin())
    );
  }

}
