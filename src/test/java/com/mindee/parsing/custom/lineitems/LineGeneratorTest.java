package com.mindee.parsing.custom.lineitems;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LineGeneratorTest {

  private static final List<Anchor> anchors = Collections.singletonList(
    new Anchor("names", 0.01d)
  );

  @Test
  void prepareLinesWith2ValuesOnEachLineWithPolygonValuesOnExactlyTheSameAxis() {
    Collection<Line> lines = LineGenerator.prepareLines(
      FakeListField.getWith2ValuesByExpectedLines(),
      anchors
    ).getLines();
    Assertions.assertEquals(2, lines.size());
  }

  @Test
  void prepareLinesWhenAnchorHasMoreLines() {
    Collection<Line> lines = LineGenerator.prepareLines(
      FakeListField.get2completeLines1halfLine(),
      Collections.singletonList(new Anchor("birthDates"))
    ).getLines();
    Assertions.assertEquals(3, lines.size());
  }

  @Test
  void prepareLinesWithTwoAnchors() {
    PreparedLines preparedLines = LineGenerator.prepareLines(
      FakeListField.get2completeLines1halfLine(),
      Arrays.asList(
          new Anchor("names"),
          new Anchor("birthDates")
      )
    );
    Assertions.assertEquals("birthDates", preparedLines.getAnchor().getName());
    Assertions.assertEquals(3, preparedLines.getLines().size());
  }

  @Test
  void prepareLinesWith1FieldValueForTheLastLine() {
    Collection<Line> lines = LineGenerator.prepareLines(
        FakeListField.getWith1FieldValueForTheLastLine(),
        anchors
    ).getLines();
    Assertions.assertEquals(3, lines.size());
  }

  @Test
  void prepareLinesWith1ExpectedLine() {
    Collection<Line> table = LineGenerator.prepareLines(
        FakeListField.getWith1ExpectedLines(),
        anchors
    ).getLines();
    Assertions.assertEquals(1, table.size());
  }

  @Test
  void prepareLinesWithPolygonsNotExactlyOnTheSameAxis() {
    List<Anchor> anchors = Collections.singletonList(
        new Anchor("names", 0.005d)
    );
    Collection<Line> table = LineGenerator.prepareLines(
        FakeListField.getWithPolygonsNotExactlyOnTheSameAxis(),
        anchors
    ).getLines();
    Assertions.assertEquals(2, table.size());
  }

  @Test
  void prepareLinesWhichRender2LinesInsteadOfOne() {
    List<Anchor> anchors = Collections.singletonList(
        new Anchor("names", 0.0d)
    );
    Collection<Line> table = LineGenerator.prepareLines(
        FakeListField.getSampleWichRender2LinesInsteadOfOne(),
        anchors
    ).getLines();
    Assertions.assertEquals(1, table.size());
  }

  @Test
  void test() {
    Assertions.assertTrue(Precision.equals(0.410, 0.420, 0.011d));
  }

}
