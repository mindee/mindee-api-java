package com.mindee.parsing.custom.lineitems;

import com.mindee.geometry.PolygonUtils;

import java.util.ArrayList;
import java.util.List;

public final class LineItemsGenerator {
  private LineItemsGenerator() {
  }

  public static LineItems generate(
      List<Field> fields,
      Anchor anchor) {

    List<Line> lines = populateLines(
        fields,
        new ArrayList<>(LineGenerator.prepareLines(fields, anchor)));

    return new LineItems(lines);
  }

  private static List<Line> populateLines(
      List<Field> fields,
      List<Line> lines) {

    List<Line> populatedLines = new ArrayList<>();

    for (Line currentLine : lines) {
      for (Field field : fields) {
        List<FieldValue> possiblesValuesRelatedToThisLine = new ArrayList<>();
        for (FieldValue fieldValue : field.getValues()) {
          double possibleValueMinY = PolygonUtils.getMinYCoordinate(fieldValue.getPolygon());

          if (possibleValueMinY < currentLine.getBbox().getMaxY()
              && possibleValueMinY >= currentLine.getBbox().getMinY()) {
            possiblesValuesRelatedToThisLine.add(fieldValue);
          }
        }
        if (!possiblesValuesRelatedToThisLine.isEmpty()) {
          currentLine.getFields()
              .put(field.getName(),
                  new Field(
                      field.getName(),
                    possiblesValuesRelatedToThisLine));
        }
      }

      populatedLines.add(currentLine);
    }

    return populatedLines;

  }
}
