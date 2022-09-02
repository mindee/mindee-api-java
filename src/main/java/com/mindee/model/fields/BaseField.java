package com.mindee.model.fields;

import java.util.List;

import com.mindee.model.geometry.Polygon;
import com.mindee.utils.geometry.BoundingBoxUtils;
import com.mindee.utils.geometry.PolygonUtils;

import lombok.Getter;

@Getter
public abstract class BaseField {

  private final Boolean reconstructed;
  private final String rawValue;
  private final Double confidence;
  private final Polygon polygon;
  private final Polygon boundingBox;
  private final Integer page;

  protected BaseField(
      Boolean reconstructed,
      String rawValue,
      Double confidence,
      List<List<Double>> polygon,
      Integer page) {
    this.reconstructed = reconstructed;
    this.rawValue = rawValue;
    this.confidence = confidence;
    this.polygon = polygon != null ? PolygonUtils.getFrom(polygon) : null;
    this.page = page;
    if (polygon != null
        && !polygon.isEmpty()) {
      this.boundingBox = BoundingBoxUtils.createFrom(this.polygon);
    } else {
      this.boundingBox = null;
    }
  }
}
