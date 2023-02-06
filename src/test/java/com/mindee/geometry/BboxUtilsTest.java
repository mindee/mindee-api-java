package com.mindee.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BboxUtilsTest {

  @Test
  public void with0PolygonMustGetNull() {
    List<Polygon> polygons = new ArrayList<>();

    Bbox bbox = BboxUtils.generate(polygons);

    Assertions.assertNull(bbox);
  }

  @Test
  public void with1PolygonAndANullPolygonMustGetNull() {
    List<Polygon> polygons = Arrays.asList(
      new Polygon(Arrays.asList(
        new Point(0.081, 0.442),
        new Point(0.15, 0.442),
        new Point(0.15, 0.451),
        new Point(0.081, 0.451))),
      null);

    Bbox bbox = BboxUtils.generate(polygons);

    Assertions.assertEquals(0.442, bbox.getMinY());
    Assertions.assertEquals(0.081, bbox.getMinX());
    Assertions.assertEquals(0.451, bbox.getMaxY());
    Assertions.assertEquals(0.15, bbox.getMaxX());
  }

  @Test
  public void withOnePolygonMustGetValidBbox() {
    List<Polygon> polygons = Arrays.asList(
      new Polygon(Arrays.asList(
        new Point(0.081, 0.442),
        new Point(0.15, 0.442),
        new Point(0.15, 0.451),
        new Point(0.081, 0.451))));

    Bbox bbox = BboxUtils.generate(polygons);

    Assertions.assertEquals(0.442, bbox.getMinY());
    Assertions.assertEquals(0.081, bbox.getMinX());
    Assertions.assertEquals(0.451, bbox.getMaxY());
    Assertions.assertEquals(0.15, bbox.getMaxX());
  }

  @Test
  public void withTwoPolygonsMustGetValidBbox() {
    List<Polygon> polygons = Arrays.asList(
      new Polygon(Arrays.asList(
        new Point(0.081, 0.442),
        new Point(0.15, 0.442),
        new Point(0.15, 0.451),
        new Point(0.081, 0.451))),
      new Polygon(Arrays.asList(
        new Point(0.157, 0.442),
        new Point(0.26, 0.442),
        new Point(0.26, 0.451),
        new Point(0.157, 0.451))));

    Bbox bbox = BboxUtils.generate(polygons);

    Assertions.assertEquals(0.442, bbox.getMinY());
    Assertions.assertEquals(0.081, bbox.getMinX());
    Assertions.assertEquals(0.451, bbox.getMaxY());
    Assertions.assertEquals(0.26, bbox.getMaxX());
  }

  @Test
  public void merge2BboxMustGetValidBbox() {
    List<Bbox> bboxs = Arrays.asList(
      new Bbox(0.081, 0.15, 0.442, 0.451),
      new Bbox(0.157, 0.26, 0.442, 0.451));

    Bbox bbox = BboxUtils.merge(bboxs);

    Assertions.assertEquals(0.442, bbox.getMinY());
    Assertions.assertEquals(0.081, bbox.getMinX());
    Assertions.assertEquals(0.451, bbox.getMaxY());
    Assertions.assertEquals(0.26, bbox.getMaxX());
  }
}
