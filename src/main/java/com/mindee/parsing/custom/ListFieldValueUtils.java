package com.mindee.parsing.custom;

import com.mindee.geometry.BoundingBoxUtils;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonUtils;

public final class ListFieldValueUtils {

  private ListFieldValueUtils() {
  }

  public static ListFieldValue merge(ListFieldValue accumulated, ListFieldValue target, String joiner) {
    Polygon mergedPolygon = PolygonUtils.combine(
      BoundingBoxUtils.createBoundingBoxFrom(accumulated.getPolygon()),
      BoundingBoxUtils.createBoundingBoxFrom(target.getPolygon()));

    String content = accumulated.getContent() == null ? target.getContent()
        : String.join(joiner,
            accumulated.getContent(), target.getContent());

    return new ListFieldValue(
      content,
      0.8,
      mergedPolygon
      );
  }
}
