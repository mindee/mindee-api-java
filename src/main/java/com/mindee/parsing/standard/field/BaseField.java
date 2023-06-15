package com.mindee.parsing.standard.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.geometry.BoundingBoxUtils;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represent basics of a field.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseField {

  /**
   * The bouding box equivalent of the polygon.
   */
  @JsonIgnore
  private final Polygon boundingBox;
  /**
   * The confidence about the zone of the value extracted.
   * A value from 0 to 1.
   */
  @JsonProperty("confidence")
  private Double confidence;
  /**
   * Define the coordinates of the zone in the page where the values has been found.
   */
  @JsonProperty("polygon")
  @JsonDeserialize(using = PolygonDeserializer.class)
  private Polygon polygon;
  /**
   * The index of the page where the current field was found.
   */
  @JsonProperty("page_id")
  private Integer pageId;

  protected BaseField(
      @JsonProperty("confidence")
      Double confidence,
      @JsonProperty("polygon")
      @JsonDeserialize(using = PolygonDeserializer.class)
      Polygon polygon,
      @JsonProperty("page_id")
      Integer pageId
  ) {
    this.confidence = confidence;
    this.polygon = polygon;
    this.pageId = pageId;
    if (polygon != null) {
      this.boundingBox = BoundingBoxUtils.createBoundingBoxFrom(this.polygon);
    } else {
      this.boundingBox = null;
    }
  }

  protected BaseField() {
    if (polygon != null) {
      this.boundingBox = BoundingBoxUtils.createBoundingBoxFrom(this.polygon);
    } else {
      this.boundingBox = null;
    }
  }
}
