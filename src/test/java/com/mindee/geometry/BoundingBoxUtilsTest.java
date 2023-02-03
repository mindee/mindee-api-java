package com.mindee.geometry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoundingBoxUtilsTest {

    @Test
    public void bboxFromARectanglePolygonMustHaveALimitedNumberOfCoordinates() {
        // given
        Polygon polygonAsRectangle = PolygonSample.getPolygonAsRectangle();

        // then
        Polygon boundingBox = BoundingBoxUtils.createBoundingBoxFrom(polygonAsRectangle);

        Assertions.assertEquals(4, boundingBox.getCoordinates().size());
    }

    @Test
    public void fromARectanglePolygonMustGetAValidBbox() {
        // given
        Polygon polygonAsRectangle = PolygonSample.getPolygonAsRectangle();

        // then
        Polygon boundingBox = BoundingBoxUtils.createBoundingBoxFrom(polygonAsRectangle);

        Assertions.assertTrue(boundingBox.getCoordinates().stream()
                .anyMatch(c -> c.getX()
                        .equals(0.123)));
        Assertions.assertTrue(boundingBox.getCoordinates().stream()
                .anyMatch(c -> c.getX()
                        .equals(0.175)));

        Assertions.assertTrue(boundingBox.getCoordinates().stream()
                .anyMatch(c -> c.getY()
                        .equals(0.53)));
        Assertions.assertTrue(boundingBox.getCoordinates().stream()
                .anyMatch(c -> c.getY()
                        .equals(0.546)));
    }

    @Test
    public void fromPolygonMustGetAValidBbox() {
        // given
        Polygon polygonWichIsNotRectangle = PolygonSample.getPolygonWichIsNotRectangle();

        // then
        Polygon boundingBox = BoundingBoxUtils.createBoundingBoxFrom(polygonWichIsNotRectangle);

        Assertions.assertTrue(boundingBox.getCoordinates().stream()
                .anyMatch(c -> c.getX()
                        .equals(0.205)));
        Assertions.assertTrue(boundingBox.getCoordinates().stream()
                .anyMatch(c -> c.getX()
                        .equals(0.381)));

        Assertions.assertTrue(boundingBox.getCoordinates().stream()
                .anyMatch(c -> c.getY()
                        .equals(0.407)));
        Assertions.assertTrue(boundingBox.getCoordinates().stream()
                .anyMatch(c -> c.getY()
                        .equals(0.43)));
    }
}
