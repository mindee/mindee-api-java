package com.mindee.model.fields;

import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonUtils;
import com.mindee.utils.GeometryUtils;
import java.util.List;
import lombok.Getter;

@Getter
public abstract class BaseField {

  private final Boolean reconstructed;
  private final String rawValue;
  private final Double confidence;
  private final Polygon polygon;
  private final List<List<Double>> bbox;
  private final Integer page;

  protected BaseField(Boolean reconstructed, String rawValue, Double confidence,
      List<List<Double>> polygon, Integer page) {
    this.reconstructed = reconstructed;
    this.rawValue = rawValue;
    this.confidence = confidence;
    this.polygon = polygon != null ? PolygonUtils.getFrom(polygon) : null;
    this.page = page;
    if (polygon != null && polygon.size() > 0) {
      this.bbox = GeometryUtils.deriveBBoxFromPolygon(polygon);
    } else {
      this.bbox = null;
    }
  }
}
