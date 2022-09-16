package com.mindee.model.customdocument;

import com.mindee.model.geometry.Polygon;
import com.mindee.utils.geometry.BoundingBoxUtils;

public final class ListFieldValue {

  private final String content;
  private final Double confidence;
  private final Polygon polygon;

  /**
   * @param content
   * @param confidence
   * @param polygon
   */
  public ListFieldValue(
      String content,
      Double confidence,
      Polygon polygon) {
    this.content = content;
    this.confidence = confidence;
    this.polygon = polygon;
  }

  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * @return the confidence
   */
  public Double getConfidence() {
    return confidence;
  }

  /**
   * @return the polygon
   */
  public Polygon getPolygon() {
    return polygon;
  }

  /**
   * @return the bbox
   */
  public Polygon getBoundingBox() {
    return BoundingBoxUtils.createBoundingBoxFrom(polygon);

  }

}
