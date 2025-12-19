package com.mindee.parsing.custom.lineitems;

import com.mindee.geometry.Point;
import com.mindee.geometry.Polygon;
import com.mindee.parsing.custom.ListField;
import com.mindee.parsing.custom.ListFieldValue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class FakeListField {
  private FakeListField() {
  }

  static Map<String, ListField> get2completeLines1halfLine() {
    Map<String, ListField> fakes = new HashMap<>();
    fakes.put(
      "birthDates",
      new ListField(
        1.0,
        Arrays.asList(
          new ListFieldValue(
            "1986-10-23",
            1.0,
            new Polygon(
              Arrays.asList(
                new Point(0.818, 0.398),
                new Point(0.902, 0.398),
                new Point(0.902, 0.406),
                new Point(0.818, 0.406)))),
          new ListFieldValue(
            "2012-02-13",
            1.0,
            new Polygon(
              Arrays.asList(
                new Point(0.819, 0.442),
                new Point(0.902, 0.442),
                new Point(0.902, 0.451),
                new Point(0.819, 0.451)))),
          new ListFieldValue(
            "1895-02-28",
            1.0,
            new Polygon(
              Arrays.asList(
                new Point(0.819, 0.462),
                new Point(0.902, 0.462),
                new Point(0.902, 0.471),
                new Point(0.819, 0.471))))
        ))
    );
    fakes.put(
      "names",
      new ListField(
        1.0,
        Arrays.asList(
          new ListFieldValue(
            "Kevin",
            1.0,
            new Polygon(
              Arrays.asList(
                new Point(0.082, 0.398),
                new Point(0.144, 0.398),
                new Point(0.144, 0.407),
                new Point(0.082, 0.407)))),
          new ListFieldValue(
            "Mindee",
            1.0,
            new Polygon(
              Arrays.asList(
                new Point(0.152, 0.398),
                new Point(0.222, 0.398),
                new Point(0.222, 0.407),
                new Point(0.152, 0.407)))),
          new ListFieldValue(
            "Ianaré",
            1.0,
            new Polygon(
              Arrays.asList(
                new Point(0.081, 0.442),
                new Point(0.15, 0.442),
                new Point(0.15, 0.451),
                new Point(0.081, 0.451)))),
          new ListFieldValue(
            "Mindee",
            1.0,
            new Polygon(
              Arrays.asList(
                new Point(0.157, 0.442),
                new Point(0.26, 0.442),
                new Point(0.26, 0.451),
                new Point(0.157, 0.451))))
        ))
    );

    return fakes;
  }

  static Map<String, ListField> getWith2ValuesByExpectedLines() {
    Map<String, ListField> fakes = new HashMap<>();
    fakes.put(
      "birthDates",
      new ListField(
        1.0,
        Arrays.asList(
          new ListFieldValue(
            "1986-10-23",
            1.0,
            new Polygon(
              Arrays.asList(
                new Point(0.818, 0.398),
                new Point(0.902, 0.398),
                new Point(0.902, 0.406),
                new Point(0.818, 0.406)))),
          new ListFieldValue(
            "2012-02-13",
            1.0,
            new Polygon(
              Arrays.asList(
                new Point(0.819, 0.442),
                new Point(0.902, 0.442),
                new Point(0.902, 0.451),
                new Point(0.819, 0.451))))
        ))
    );

    fakes.put(
      "names",
      new ListField(
        1.0,
        Arrays.asList(
            new ListFieldValue(
                "Kevin",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.082, 0.398),
                        new Point(0.144, 0.398),
                        new Point(0.144, 0.407),
                        new Point(0.082, 0.407)))),
            new ListFieldValue(
                "Mindee",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.152, 0.398),
                        new Point(0.222, 0.398),
                        new Point(0.222, 0.407),
                        new Point(0.152, 0.407)))),
            new ListFieldValue(
                "Ianaré",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.081, 0.442),
                        new Point(0.15, 0.442),
                        new Point(0.15, 0.451),
                        new Point(0.081, 0.451)))),
            new ListFieldValue(
                "Mindee",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.157, 0.442),
                        new Point(0.26, 0.442),
                        new Point(0.26, 0.451),
                        new Point(0.157, 0.451))))
        ))
    );

    return fakes;
  }

  static Map<String, ListField> getWith1FieldValueForTheLastLine() {
    Map<String, ListField> fakes = new HashMap<>();

    fakes.put(
      "birthDates",
      new ListField(
        1.0,
        Arrays.asList(
            new ListFieldValue(
                "1986-10-23",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.818, 0.398),
                        new Point(0.902, 0.398),
                        new Point(0.902, 0.406),
                        new Point(0.818, 0.406)))),
            new ListFieldValue(
                "2012-02-13",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.819, 0.442),
                        new Point(0.902, 0.442),
                        new Point(0.902, 0.451),
                        new Point(0.819, 0.451))))
        ))
    );

    fakes.put(
      "names",
      new ListField(
        1.0,
        Arrays.asList(
            new ListFieldValue(
                "Kevin",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.082, 0.398),
                        new Point(0.144, 0.398),
                        new Point(0.144, 0.407),
                        new Point(0.082, 0.407)))),
            new ListFieldValue(
                "Mindee",
              1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.152, 0.398),
                        new Point(0.222, 0.398),
                        new Point(0.222, 0.407),
                        new Point(0.152, 0.407)))),
            new ListFieldValue(
                "Ianaré",
              1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.081, 0.442),
                        new Point(0.15, 0.442),
                        new Point(0.15, 0.451),
                        new Point(0.081, 0.451)))),
            new ListFieldValue(
                "Mindee",
              1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.157, 0.442),
                        new Point(0.26, 0.442),
                        new Point(0.26, 0.451),
                        new Point(0.157, 0.451)))),
            new ListFieldValue(
                "Bob",
              1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.082, 0.486),
                        new Point(0.151, 0.486),
                        new Point(0.151, 0.495),
                        new Point(0.082, 0.495))))
        ))
    );

    return fakes;
  }

  static Map<String, ListField> getWith1ExpectedLines() {
    Map<String, ListField> fakes = new HashMap<>();
    fakes.put(
      "birthDates",
      new ListField(
        1.0,
          Arrays.asList(
              new ListFieldValue(
                  "1986-10-23",
                  1.0,
                  new Polygon(
                      Arrays.asList(
                          new Point(0.818, 0.398),
                          new Point(0.902, 0.398),
                          new Point(0.902, 0.406),
                          new Point(0.818, 0.406))))
          ))
    );
    fakes.put(
      "names",
      new ListField(
        1.0,
        Arrays.asList(
            new ListFieldValue(
                "Kevin",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.082, 0.398),
                        new Point(0.144, 0.398),
                        new Point(0.144, 0.407),
                        new Point(0.082, 0.407)))),
            new ListFieldValue(
                "Mindee",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.152, 0.398),
                        new Point(0.222, 0.398),
                        new Point(0.222, 0.407),
                        new Point(0.152, 0.407))))
        ))
    );
    return fakes;
  }

  static Map<String, ListField> getWithPolygonsNotExactlyOnTheSameAxis() {
    Map<String, ListField> fakes = new HashMap<>();
    fakes.put(
      "birthDates",
      new ListField(
        1.0,
        Arrays.asList(
            new ListFieldValue(
                "1986-10-23",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.576, 0.401),
                        new Point(0.649, 0.401),
                        new Point(0.649, 0.408),
                        new Point(0.576, 0.408)))),
            new ListFieldValue(
                "2012-02-13",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.581, 0.45),
                        new Point(0.656, 0.45),
                        new Point(0.656, 0.458),
                        new Point(0.581, 0.458))))
        ))
    );
    fakes.put(
      "names",
      new ListField(
        1.0,
        Arrays.asList(
            new ListFieldValue(
                "Kevin",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.119, 0.4),
                        new Point(0.179, 0.4),
                        new Point(0.178, 0.41),
                        new Point(0.119, 0.409)))),
            new ListFieldValue(
                "Mindee",
              1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.185, 0.401),
                        new Point(0.232, 0.401),
                        new Point(0.232, 0.41),
                        new Point(0.184, 0.409)))),
            new ListFieldValue(
                "Ianaré",
              1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.118, 0.45),
                        new Point(0.169, 0.451),
                        new Point(0.169, 0.458),
                        new Point(0.117, 0.457))))
        ))
    );
    return fakes;
  }

  static Map<String, ListField> getSampleWichRender2LinesInsteadOfOne() {
    Map<String, ListField> fakes = new HashMap<>();
    fakes.put(
      "names",
      new ListField(
        1.0,
        Arrays.asList(
            new ListFieldValue(
                "A",
                1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.075, 0.42),
                        new Point(0.141, 0.42),
                        new Point(0.141, 0.428),
                        new Point(0.075, 0.428)))),
            new ListFieldValue(
                "B",
              1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.148, 0.42),
                        new Point(0.198, 0.42),
                        new Point(0.198, 0.428),
                        new Point(0.148, 0.428)))),
            new ListFieldValue(
                "C",
              1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.2, 0.42),
                        new Point(0.204, 0.42),
                        new Point(0.204, 0.428),
                        new Point(0.2, 0.428)))),
            new ListFieldValue(
                "D",
              1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.206, 0.42),
                        new Point(0.257, 0.42),
                        new Point(0.257, 0.428),
                        new Point(0.206, 0.428)))),
            new ListFieldValue(
                "E",
              1.0,
                new Polygon(
                    Arrays.asList(
                        new Point(0.263, 0.42),
                        new Point(0.33, 0.42),
                        new Point(0.33, 0.428),
                        new Point(0.263, 0.428))))
        ))
    );
    return fakes;
  }
}
