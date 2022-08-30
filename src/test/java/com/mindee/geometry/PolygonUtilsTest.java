package com.mindee.geometry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PolygonUtilsTest {
  @Test
  public void givenAValidPolygonMustGetTheValidCentroid() {
    // given
    Polygon polygonWichIsNotRectangle = PolygonSample.getPolygonAsRectangle();

    // then
    Point centroid = PolygonUtils.getCentroid(polygonWichIsNotRectangle);

    Assertions.assertEquals(new Point(0.149, 0.538),
        centroid);
  }

  @Test
  public void givenAValidPolygonMustGetTheMaxX() {
    // given
    Polygon polygonWichIsNotRectangle = PolygonSample.getPolygonAsRectangle();

    // then
    Double maxX = PolygonUtils.getMaxXCoordinate(polygonWichIsNotRectangle);

    Assertions.assertEquals(0.175, maxX);
  }

  public void givenAValidPolygonMustGetTheMinX() {
    // given
    Polygon polygonWichIsNotRectangle = PolygonSample.getPolygonAsRectangle();

    // then
    Double minX = PolygonUtils.getMinXCoordinate(polygonWichIsNotRectangle);

    Assertions.assertEquals(0.175, minX);
  }

  @Test
  public void givenAValidPolygonMustGetTheMaxY() {
    // given
    Polygon polygonWichIsNotRectangle = PolygonSample.getPolygonAsRectangle();

    // then
    Double maxY = PolygonUtils.getMaxYCoordinate(polygonWichIsNotRectangle);

    Assertions.assertEquals(0.546, maxY);
  }

  @Test
  public void givenAValidPolygonMustGetTheMinY() {
    // given
    Polygon polygonWichIsNotRectangle = PolygonSample.getPolygonAsRectangle();

    // then
    Double minY = PolygonUtils.getMinYCoordinate(polygonWichIsNotRectangle);

    Assertions.assertEquals(0.53, minY);
  }
}
