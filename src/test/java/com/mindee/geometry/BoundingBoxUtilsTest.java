package com.mindee.geometry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoundingBoxUtilsTest {

  @Test
  public void fromARectanglePolygonMustGetAValidBbox() {
    // given
    Polygon polygonAsRectangle = PolygonSample.getPolygonAsRectangle();

    // then
    BoundingBox boundingBox = BoundingBoxUtils.createFrom(polygonAsRectangle);

    Assertions.assertEquals(0.123, boundingBox.getMinX());
    Assertions.assertEquals(0.175, boundingBox.getMaxX());
    Assertions.assertEquals(0.53, boundingBox.getMinY());
    Assertions.assertEquals(0.546, boundingBox.getMaxY());
  }

  @Test
  public void fromPolygonMustGetAValidBbox() {
    // given
    Polygon polygonWichIsNotRectangle = PolygonSample.getPolygonWichIsNotRectangle();

    // then
    BoundingBox boundingBox = BoundingBoxUtils.createFrom(polygonWichIsNotRectangle);

    Assertions.assertEquals(0.205, boundingBox.getMinX());
    Assertions.assertEquals(0.381, boundingBox.getMaxX());
    Assertions.assertEquals(0.43, boundingBox.getMaxY());
    Assertions.assertEquals(0.407, boundingBox.getMinY());
  }
}
