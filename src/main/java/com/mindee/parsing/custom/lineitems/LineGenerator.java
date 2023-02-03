package com.mindee.parsing.custom.lineitems;

import com.mindee.geometry.Bbox;
import com.mindee.geometry.BboxUtils;
import org.apache.commons.math3.util.Precision;

import java.util.*;

public final class LineGenerator {

  private LineGenerator() {
  }

  public static Collection<Line> prepareLines(
      List<Field> fields,
      Anchor fieldAsAnchor) {
    HashMap<Integer, Line> table = new HashMap<>();

    Optional<Field> anchor = fields.stream()
        .filter(f -> f.getName().equals(fieldAsAnchor.getName())).findFirst();

    if (!anchor.isPresent()) {
      throw new IllegalStateException("The field selected for the anchor was not found.");
    }

    if (anchor.get().getValues().isEmpty()) {
      throw new IllegalStateException("No lines have been detected.");
    }

    // handle one value and the case of one line
    int lineNumber = 1;
    Line currentLine = new Line(lineNumber, fieldAsAnchor.getTolerance());
    FieldValue currentValue = anchor.get().getValues().get(0);
    currentLine.setBbox(BboxUtils.generate(currentValue.getPolygon()));

    for (int i = 1; i < anchor.get().getValues().size(); i++) {

      currentValue = anchor.get().getValues().get(i);
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

    if (!table.containsKey(lineNumber)) {
      table.put(lineNumber, currentLine);
    }

    return table.values();
  }

}
