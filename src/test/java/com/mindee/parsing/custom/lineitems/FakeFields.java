package com.mindee.parsing.custom.lineitems;

import java.util.Arrays;
import java.util.List;

import com.mindee.geometry.Point;
import com.mindee.geometry.Polygon;

class FakeFields {
  private FakeFields() {
  }

  static List<Field> getWith2ValuesByExpectedLines() {
    return Arrays.asList(
        new Field(
            "birthDates",
            Arrays.asList(
                new FieldValue(
                    "1986-10-23",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.818, 0.398),
                            new Point(0.902, 0.398),
                            new Point(0.902, 0.406),
                            new Point(0.818, 0.406)))),
                new FieldValue(
                    "2012-02-13",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.819, 0.442),
                            new Point(0.902, 0.442),
                            new Point(0.902, 0.451),
                            new Point(0.819, 0.451)))))),
        new Field(
            "names",
            Arrays.asList(
                new FieldValue(
                    "Kevin",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.082, 0.398),
                            new Point(0.144, 0.398),
                            new Point(0.144, 0.407),
                            new Point(0.082, 0.407)))),
                new FieldValue(
                    "Mindee",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.152, 0.398),
                            new Point(0.222, 0.398),
                            new Point(0.222, 0.407),
                            new Point(0.152, 0.407)))),
                new FieldValue(
                    "Ianaré",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.081, 0.442),
                            new Point(0.15, 0.442),
                            new Point(0.15, 0.451),
                            new Point(0.081, 0.451)))),
                new FieldValue(
                    "Mindee",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.157, 0.442),
                            new Point(0.26, 0.442),
                            new Point(0.26, 0.451),
                            new Point(0.157, 0.451)))))));
  }

  static List<Field> getWith1FieldValueForTheLastLine() {
    return Arrays.asList(
        new Field(
            "birthDates",
            Arrays.asList(
                new FieldValue(
                    "1986-10-23",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.818, 0.398),
                            new Point(0.902, 0.398),
                            new Point(0.902, 0.406),
                            new Point(0.818, 0.406)))),
                new FieldValue(
                    "2012-02-13",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.819, 0.442),
                            new Point(0.902, 0.442),
                            new Point(0.902, 0.451),
                            new Point(0.819, 0.451)))))),
        new Field(
            "names",
            Arrays.asList(
                new FieldValue(
                    "Kevin",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.082, 0.398),
                            new Point(0.144, 0.398),
                            new Point(0.144, 0.407),
                            new Point(0.082, 0.407)))),
                new FieldValue(
                    "Mindee",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.152, 0.398),
                            new Point(0.222, 0.398),
                            new Point(0.222, 0.407),
                            new Point(0.152, 0.407)))),
                new FieldValue(
                    "Ianaré",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.081, 0.442),
                            new Point(0.15, 0.442),
                            new Point(0.15, 0.451),
                            new Point(0.081, 0.451)))),
                new FieldValue(
                    "Mindee",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.157, 0.442),
                            new Point(0.26, 0.442),
                            new Point(0.26, 0.451),
                            new Point(0.157, 0.451)))),
                new FieldValue(
                    "Bob",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.082, 0.486),
                            new Point(0.151, 0.486),
                            new Point(0.151, 0.495),
                            new Point(0.082, 0.495)))))));
  }

  static List<Field> getWith1ExpectedLines() {
    return Arrays.asList(
        new Field(
            "birthDates",
            Arrays.asList(
                new FieldValue(
                    "1986-10-23",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.818, 0.398),
                            new Point(0.902, 0.398),
                            new Point(0.902, 0.406),
                            new Point(0.818, 0.406)))))),
        new Field(
            "names",
            Arrays.asList(
                new FieldValue(
                    "Kevin",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.082, 0.398),
                            new Point(0.144, 0.398),
                            new Point(0.144, 0.407),
                            new Point(0.082, 0.407)))),
                new FieldValue(
                    "Mindee",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.152, 0.398),
                            new Point(0.222, 0.398),
                            new Point(0.222, 0.407),
                            new Point(0.152, 0.407)))))));
  }

  static List<Field> getWithPolygonsNotExactlyOnTheSameAxis() {
    return Arrays.asList(
        new Field(
            "birthDates",
            Arrays.asList(
                new FieldValue(
                    "1986-10-23",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.576, 0.401),
                            new Point(0.649, 0.401),
                            new Point(0.649, 0.408),
                            new Point(0.576, 0.408)))),
                new FieldValue(
                    "2012-02-13",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.581, 0.45),
                            new Point(0.656, 0.45),
                            new Point(0.656, 0.458),
                            new Point(0.581, 0.458)))))),
        new Field(
            "names",
            Arrays.asList(
                new FieldValue(
                    "Kevin",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.119, 0.4),
                            new Point(0.179, 0.4),
                            new Point(0.178, 0.41),
                            new Point(0.119, 0.409)))),
                new FieldValue(
                    "Mindee",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.185, 0.401),
                            new Point(0.232, 0.401),
                            new Point(0.232, 0.41),
                            new Point(0.184, 0.409)))),
                new FieldValue(
                    "Ianaré",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.118, 0.45),
                            new Point(0.169, 0.451),
                            new Point(0.169, 0.458),
                            new Point(0.117, 0.457)))))));
  }

  static List<Field> getSampleWichRender2LinesInsteadOfOne() {
    return Arrays.asList(
        new Field(
            "names",
            Arrays.asList(
                new FieldValue(
                    "A",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.075, 0.42),
                            new Point(0.141, 0.42),
                            new Point(0.141, 0.428),
                            new Point(0.075, 0.428)))),
                new FieldValue(
                    "B",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.148, 0.42),
                            new Point(0.198, 0.42),
                            new Point(0.198, 0.428),
                            new Point(0.148, 0.428)))),
                new FieldValue(
                    "C",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.2, 0.42),
                            new Point(0.204, 0.42),
                            new Point(0.204, 0.428),
                            new Point(0.2, 0.428)))),
                new FieldValue(
                    "D",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.206, 0.42),
                            new Point(0.257, 0.42),
                            new Point(0.257, 0.428),
                            new Point(0.206, 0.428)))),
                new FieldValue(
                    "E",
                    new Polygon(
                        Arrays.asList(
                            new Point(0.263, 0.42),
                            new Point(0.33, 0.42),
                            new Point(0.33, 0.428),
                            new Point(0.263, 0.428)))))));
  }
}
