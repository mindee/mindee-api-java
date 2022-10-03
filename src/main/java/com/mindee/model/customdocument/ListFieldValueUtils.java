package com.mindee.model.customdocument;

import com.mindee.model.geometry.Polygon;
import com.mindee.utils.geometry.PolygonUtils;

public class ListFieldValueUtils {
  private ListFieldValueUtils() {
  }

  public static ListFieldValue merge(ListFieldValue accumulated, ListFieldValue target, String joiner) {
    Polygon mergedPolygon = PolygonUtils.combine(accumulated.getBoundingBox(), target.getBoundingBox());

    String content = accumulated.getContent() == null ? target.getContent()
        : String.join(joiner,
            accumulated.getContent(), target.getContent());

    return new ListFieldValue(
        content,
        0.0,
        mergedPolygon);
  }
}
