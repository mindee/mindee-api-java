package com.mindee.parsing.custom.lineitems;

import com.mindee.geometry.Bbox;
import com.mindee.geometry.BboxUtils;
import com.mindee.parsing.custom.ListField;
import com.mindee.parsing.custom.ListFieldValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Precision;


/**
 * Get lines.
 */
public final class LineGenerator {

  private LineGenerator() {
  }

  public static PreparedLines prepareLines(
      Map<String, ListField> fields,
      List<Anchor> anchors
  ) {
    Anchor bestAnchor = null;
    Collection<Line> lines = null;
    int lineCount = 0;
    for (Anchor anchor : anchors) {
      ListField anchorColumn = fields.get(anchor.getName());
      if (anchorColumn == null) {
        throw new IllegalStateException("The field selected for the anchor was not found.");
      }
      Collection<Line> currentLines = createLines(
          anchorColumn.getValues(),
          anchor.getTolerance()
      );
      if (currentLines.size() > lineCount) {
        bestAnchor = anchor;
        lines = currentLines;
        lineCount = currentLines.size();
      }
    }
    if (lines == null) {
      throw new IllegalStateException("Could not determine which anchor to use.");
    }
    return new PreparedLines(
        bestAnchor,
        new ArrayList<>(lines)
    );
  }

  private static Collection<Line> createLines(List<ListFieldValue> anchorValues, double tolerance) {
    HashMap<Integer, Line> table = new HashMap<>();

    // handle one value and the case of one line
    int lineNumber = 1;
    Line currentLine = new Line(lineNumber, tolerance);
    ListFieldValue currentValue = anchorValues.get(0);
    currentLine.setBbox(currentValue.getPolygon().getAsBbox());
    for (ListFieldValue anchorValue : anchorValues) {
      currentValue = anchorValue;
      Bbox currentFieldBbox = currentValue.getPolygon().getAsBbox();
      if (
          Precision.equals(
            currentLine.getBbox().getMinY(),
            currentFieldBbox.getMinY(),
            tolerance
          )
      ) {
        currentLine.setBbox(
            BboxUtils.merge(Arrays.asList(currentLine.getBbox(), currentFieldBbox))
        );
      } else {
        // when it is a new line
        table.put(lineNumber, currentLine);
        lineNumber++;
        currentLine = new Line(lineNumber, tolerance);
        currentLine.setBbox(currentFieldBbox);
      }
    }
    table.putIfAbsent(lineNumber, currentLine);
    return table.values();
  }

}
