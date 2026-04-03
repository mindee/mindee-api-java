package com.mindee.geometry;

import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PolygonUtilsTest {
  @Test
  public void givenAValidPolygonMustGetTheValidCentroid() {
    Polygon nonRectangularPolygon = PolygonSample.getPolygonAsRectangle();
    Point centroid = nonRectangularPolygon.getCentroid();
    Assertions.assertEquals(new Point(0.149, 0.538), centroid);
  }

  @Test
  public void givenAValidPolygonMustGetTheMaxX() {
    Polygon nonRectangularPolygon = PolygonSample.getPolygonAsRectangle();
    Double maxX = nonRectangularPolygon.getMinMaxX().getMax();
    Assertions.assertEquals(0.175, maxX);
  }

  @Test
  public void givenAValidPolygonMustGetTheMinX() {
    Polygon nonRectangularPolygon = PolygonSample.getPolygonAsRectangle();
    Double minX = nonRectangularPolygon.getMinMaxX().getMin();
    Assertions.assertEquals(0.123, minX);
  }

  @Test
  public void givenAValidPolygonMustGetTheMaxY() {
    Polygon nonRectangularPolygon = PolygonSample.getPolygonAsRectangle();
    Double maxY = nonRectangularPolygon.getMinMaxY().getMax();
    Assertions.assertEquals(0.546, maxY);
  }

  @Test
  public void givenAValidPolygonMustGetTheMinY() {
    Polygon nonRectangularPolygon = PolygonSample.getPolygonAsRectangle();
    Double minY = nonRectangularPolygon.getMinMaxY().getMin();
    Assertions.assertEquals(0.53, minY);
  }

  @Test
  public void combinePolygonsWithTwoNotNullMustGetAValidPolygon() {
    // given
    Polygon polygon1 = new Polygon(
      Arrays
        .asList(
          new Point(0.081, 0.442),
          new Point(0.15, 0.442),
          new Point(0.15, 0.451),
          new Point(0.081, 0.451)
        )
    );

    Polygon polygon2 = new Polygon(
      Arrays
        .asList(
          new Point(0.157, 0.442),
          new Point(0.26, 0.442),
          new Point(0.26, 0.451),
          new Point(0.157, 0.451)
        )
    );

    // then
    Polygon mergedPolygon = polygon1.combine(polygon2);

    Assertions.assertEquals(0.081, mergedPolygon.getMinMaxX().getMin());
    Assertions.assertEquals(0.26, mergedPolygon.getMinMaxX().getMax());
    Assertions.assertEquals(0.442, mergedPolygon.getMinMaxY().getMin());
    Assertions.assertEquals(0.451, mergedPolygon.getMinMaxY().getMax());
  }

  @Test
  public void combineWithNullPolygonMustThrow() {
    assertThrows(IllegalStateException.class, () -> {
      Polygon polygon = PolygonUtils.combine(null, null);
    });
  }

  @Test
  public void combineWith1PolygonAndANullPolygonMustGetNull() {
    Polygon polygon1 = new Polygon(
      Arrays
        .asList(
          new Point(0.081, 0.442),
          new Point(0.15, 0.442),
          new Point(0.15, 0.451),
          new Point(0.081, 0.451)
        )
    );

    Polygon mergedPolygon = polygon1.combine(null);

    Assertions.assertEquals(0.081, mergedPolygon.getMinMaxX().getMin());
    Assertions.assertEquals(0.15, mergedPolygon.getMinMaxX().getMax());
    Assertions.assertEquals(0.442, mergedPolygon.getMinMaxY().getMin());
    Assertions.assertEquals(0.451, mergedPolygon.getMinMaxY().getMax());
  }

}
