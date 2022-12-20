package com.mindee.parsing.common.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.model.deserialization.PolygonDeserializer;
import com.mindee.model.geometry.Polygon;
import com.mindee.utils.geometry.BoundingBoxUtils;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseField {

  @JsonProperty("confidence")
  private Double confidence;
  @JsonProperty("polygon")
  @JsonDeserialize(using = PolygonDeserializer.class)
  private Polygon polygon;
  @JsonProperty("page_id")
  private Integer id;
  @JsonIgnore
  private final Polygon boundingBox;

  protected BaseField() {
    if (polygon != null) {
      this.boundingBox = BoundingBoxUtils.createBoundingBoxFrom(this.polygon);
    } else {
      this.boundingBox = null;
    }
  }
}
