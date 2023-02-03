package com.mindee.parsing.custom.lineitems;

import com.mindee.geometry.BoundingBoxUtils;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonUtils;

public class FieldValueUtils {
  private FieldValueUtils() {
  }

  public static FieldValue merge(FieldValue accumulated, FieldValue target, String joiner) {
    Polygon mergedPolygon = PolygonUtils.combine(
      BoundingBoxUtils.createBoundingBoxFrom(accumulated.getPolygon()),
      BoundingBoxUtils.createBoundingBoxFrom(target.getPolygon()));

    String content = accumulated.getContent() == null ? target.getContent()
        : String.join(joiner,
            accumulated.getContent(), target.getContent());

    return new FieldValue(
        content,
        mergedPolygon);
  }
}
