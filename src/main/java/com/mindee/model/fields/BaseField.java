package com.mindee.model.fields;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindee.model.geometry.Polygon;
import com.mindee.utils.geometry.BoundingBoxUtils;
import lombok.Getter;

@Getter
public abstract class BaseField {

  private final Boolean reconstructed;
  private final Double confidence;
  private final Polygon polygon;
  @JsonIgnore
  private final Polygon boundingBox;
  private final Integer page;

  protected BaseField(
      Boolean reconstructed,
      Double confidence,
      Polygon polygon,
      Integer page) {
    this.reconstructed = reconstructed;
    this.confidence = confidence;
    this.polygon = polygon;
    this.page = page;
    if (polygon != null) {
      this.boundingBox = BoundingBoxUtils.createBoundingBoxFrom(this.polygon);
    } else {
      this.boundingBox = null;
    }
  }
}
