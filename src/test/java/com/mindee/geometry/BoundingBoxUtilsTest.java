package com.mindee.geometry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoundingBoxUtilsTest {

  @Test
  public void bboxFromARectanglePolygonMustHaveALimitedNumberOfCoordinates() {
    Polygon polygonAsRectangle = PolygonSample.getPolygonAsRectangle();
    Polygon boundingBox = polygonAsRectangle.getBoundingBox();

    Assertions.assertEquals(4, boundingBox.getCoordinates().size());
  }

  @Test
  public void fromARectanglePolygonMustGetAValidBbox() {
    Polygon polygonAsRectangle = PolygonSample.getPolygonAsRectangle();
    Polygon boundingBox = polygonAsRectangle.getBoundingBox();

    Assertions
      .assertTrue(boundingBox.getCoordinates().stream().anyMatch(c -> c.getX().equals(0.123)));
    Assertions
      .assertTrue(boundingBox.getCoordinates().stream().anyMatch(c -> c.getX().equals(0.175)));

    Assertions
      .assertTrue(boundingBox.getCoordinates().stream().anyMatch(c -> c.getY().equals(0.53)));
    Assertions
      .assertTrue(boundingBox.getCoordinates().stream().anyMatch(c -> c.getY().equals(0.546)));
  }

  @Test
  public void fromPolygonMustGetAValidBbox() {
    Polygon polygonWichIsNotRectangle = PolygonSample.getPolygonWichIsNotRectangle();
    Polygon boundingBox = polygonWichIsNotRectangle.getBoundingBox();

    Assertions
      .assertTrue(boundingBox.getCoordinates().stream().anyMatch(c -> c.getX().equals(0.205)));
    Assertions
      .assertTrue(boundingBox.getCoordinates().stream().anyMatch(c -> c.getX().equals(0.381)));

    Assertions
      .assertTrue(boundingBox.getCoordinates().stream().anyMatch(c -> c.getY().equals(0.407)));
    Assertions
      .assertTrue(boundingBox.getCoordinates().stream().anyMatch(c -> c.getY().equals(0.43)));
  }
}
