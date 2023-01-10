package com.mindee.geometry;

import static org.junit.Assert.assertThrows;

import com.mindee.utils.geometry.PolygonUtils;
import java.util.Arrays;
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

  @Test
  public void combinePolygonsWithTwoNotNullMustGetAValidPolygon() {
    // given
    Polygon polygon1 = new Polygon(Arrays.asList(
        new Point(0.081, 0.442),
        new Point(0.15, 0.442),
        new Point(0.15, 0.451),
        new Point(0.081, 0.451)));

    Polygon polygon2 = new Polygon(Arrays.asList(
        new Point(0.157, 0.442),
        new Point(0.26, 0.442),
        new Point(0.26, 0.451),
        new Point(0.157, 0.451)));

    // then
    Polygon mergedPolygon = PolygonUtils.combine(polygon1, polygon2);

    Assertions.assertEquals(0.442, PolygonUtils.getMinYCoordinate(mergedPolygon));
    Assertions.assertEquals(0.081, PolygonUtils.getMinXCoordinate(mergedPolygon));
    Assertions.assertEquals(0.451, PolygonUtils.getMaxYCoordinate(mergedPolygon));
    Assertions.assertEquals(0.26, PolygonUtils.getMaxXCoordinate(mergedPolygon));
  }

  @Test
  public void combineWithNullPolygonMustThrow() {
    assertThrows(IllegalStateException.class, () -> {
      Polygon polygon = PolygonUtils.combine(null, null);
    });

  }

  @Test
  public void combineWith1PolygonAndANullPolygonMustGetNull() {
    // given
    Polygon polygon1 = new Polygon(Arrays.asList(
        new Point(0.081, 0.442),
        new Point(0.15, 0.442),
        new Point(0.15, 0.451),
        new Point(0.081, 0.451)));

    Polygon polygon2 = null;

    // then
    Polygon mergedPolygon = PolygonUtils.combine(polygon1, polygon2);

    Assertions.assertEquals(0.442, PolygonUtils.getMinYCoordinate(mergedPolygon));
    Assertions.assertEquals(0.081, PolygonUtils.getMinXCoordinate(mergedPolygon));
    Assertions.assertEquals(0.451, PolygonUtils.getMaxYCoordinate(mergedPolygon));
    Assertions.assertEquals(0.15, PolygonUtils.getMaxXCoordinate(mergedPolygon));
  }

}
