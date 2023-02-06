package com.mindee.parsing.custom.lineitems;

import java.util.Collection;

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LineGeneratorTest {

  @Test
  void prepareLinesWith2ValuesOnEachLineWithPolygonValuesOnExactlyTheSameAxis() {
    Anchor anchor = new Anchor("names");

    Collection<Line> table = LineGenerator.prepareLines(
      FakeListField.getWith2ValuesByExpectedLines(),
      anchor
    );

    Assertions.assertEquals(2, table.size());
  }

  @Test
  void prepareLinesWith1FieldValueForTheLastLine() {
    Anchor anchor = new Anchor("names");

    Collection<Line> table = LineGenerator.prepareLines(
        FakeListField.getWith1FieldValueForTheLastLine(), anchor);

    Assertions.assertEquals(3, table.size());
  }

  @Test
  void prepareLinesWith1ExpectedLine() {
    Anchor anchor = new Anchor("names");

    Collection<Line> table = LineGenerator.prepareLines(
        FakeListField.getWith1ExpectedLines(), anchor);

    Assertions.assertEquals(1, table.size());
  }

  @Test
  void prepareLinesWithPolygonsNotExactlyOnTheSameAxis() {
    Anchor anchor = new Anchor("names", 0.005d);

    Collection<Line> table = LineGenerator.prepareLines(
        FakeListField.getWithPolygonsNotExactlyOnTheSameAxis(), anchor);

    Assertions.assertEquals(2, table.size());
  }

  @Test
  void prepareLinesWhichRender2LinesInsteadOfOne() {
    Anchor anchor = new Anchor("names", 0.0d);

    Collection<Line> table = LineGenerator.prepareLines(
        FakeListField.getSampleWichRender2LinesInsteadOfOne(), anchor);

    Assertions.assertEquals(1, table.size());
  }

  @Test
  void test() {
    Assertions.assertTrue(Precision.equals(0.410, 0.420, 0.011d));
  }

}
