package com.mindee.parsing.custom.lineitems;

import com.mindee.geometry.PolygonUtils;
import com.mindee.parsing.custom.ListField;
import com.mindee.parsing.custom.ListFieldValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * WARNING: This feature is experimental!
 * Results may not always work as intended.
 * Don't use unless you know what you're doing ;-)
 */
public final class LineItemsGenerator {
  private LineItemsGenerator() {
  }

  /**
   * WARNING: This feature is experimental!
   * Results may not always work as intended.
   * Don't use unless you know what you're doing ;-)
   */
  public static LineItems generate(
    List<String> fieldNames,
    Map<String, ListField> fields,
    Anchor anchor) {

    Map<String, ListField> fieldsToTransformIntoLines = fields.entrySet()
      .stream()
      .filter(field -> fieldNames.contains(field.getKey()))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    List<Line> lines = populateLines(
      fieldsToTransformIntoLines,
      new ArrayList<>(LineGenerator.prepareLines(fieldsToTransformIntoLines, anchor)));

    return new LineItems(lines);
  }

  private static List<Line> populateLines(
    Map<String, ListField> fields,
    List<Line> lines) {

    List<Line> populatedLines = new ArrayList<>();

    for (Line currentLine : lines) {
      for (Map.Entry<String, ListField> field : fields.entrySet()) {
        for (ListFieldValue listFieldValue : field.getValue().getValues()) {
          double minYCurrentValue = PolygonUtils.getMinYCoordinate(listFieldValue.getPolygon());

          if (minYCurrentValue < currentLine.getBbox().getMaxY()
            && minYCurrentValue >= currentLine.getBbox().getMinY()) {
            currentLine.addField(field.getKey(), listFieldValue);
          }
        }
      }

      populatedLines.add(currentLine);
    }

    return populatedLines;

  }
}
