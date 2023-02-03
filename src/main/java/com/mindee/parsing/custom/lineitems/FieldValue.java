package com.mindee.parsing.custom.lineitems;

import com.mindee.geometry.Polygon;
import com.mindee.geometry.BoundingBoxUtils;
import lombok.Getter;

@Getter
public final class FieldValue {

  private final String content;
  private final Polygon polygon;

  /**
   * @param content
   * @param polygon
   */
  public FieldValue(
      String content,
      Polygon polygon) {
    this.content = content;
    this.polygon = polygon;
  }

  /**
   * @return the bbox
   */
  public Polygon getBoundingBox() {
    return BoundingBoxUtils.createBoundingBoxFrom(polygon);

  }

}
