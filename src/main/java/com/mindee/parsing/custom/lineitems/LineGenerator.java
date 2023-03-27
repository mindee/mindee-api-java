package com.mindee.parsing.custom.lineitems;

import com.mindee.geometry.Bbox;
import com.mindee.geometry.BboxUtils;
import com.mindee.parsing.custom.ListField;
import com.mindee.parsing.custom.ListFieldValue;
import org.apache.commons.math3.util.Precision;

import java.util.*;

public final class LineGenerator {

  private LineGenerator() {
  }

  public static Collection<Line> prepareLines(
    Map<String, ListField> fields,
    Anchor fieldAsAnchor) {
    HashMap<Integer, Line> table = new HashMap<>();

    ListField anchor = fields.get(fieldAsAnchor.getName());

    if (anchor == null) {
      throw new IllegalStateException("The field selected for the anchor was not found.");
    }

    if (anchor.getValues().isEmpty()) {
      throw new IllegalStateException("No lines have been detected.");
    }

    // handle one value and the case of one line
    int lineNumber = 1;
    Line currentLine = new Line(lineNumber, fieldAsAnchor.getTolerance());
    ListFieldValue currentValue = anchor.getValues().get(0);
    currentLine.setBbox(BboxUtils.generate(currentValue.getPolygon()));

    for (int i = 1; i < anchor.getValues().size(); i++) {

      currentValue = anchor.getValues().get(i);
      Bbox currentFieldBbox = BboxUtils.generate(currentValue.getPolygon());

      if (Precision.equals(
        currentLine.getBbox().getMinY(),
        currentFieldBbox.getMinY(),
        fieldAsAnchor.getTolerance())) {
        currentLine.setBbox(BboxUtils
          .merge(
            Arrays.asList(currentLine.getBbox(), currentFieldBbox)));
      } else {
        // when it is a new line
        table.put(lineNumber, currentLine);
        lineNumber++;
        currentLine = new Line(lineNumber, fieldAsAnchor.getTolerance());
        currentLine.setBbox(currentFieldBbox);
      }
    }

    table.putIfAbsent(lineNumber, currentLine);

    return table.values();
  }

}
