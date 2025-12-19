package com.mindee.parsing.custom.lineitems;

import com.mindee.geometry.MinMax;
import com.mindee.parsing.custom.ListField;
import com.mindee.parsing.custom.ListFieldValue;
import java.util.ArrayList;
import java.util.Collections;
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
   * Generate line items.
   * Use this method if you want to send a list of different possible anchor fields.
   * Will use the tolerance settings from the best anchor.
   */
  public static LineItems generate(
      List<String> fieldNames,
      Map<String, ListField> fields,
      List<Anchor> anchors
  ) {
    Map<String, ListField> fieldsToTransformIntoLines = fields
      .entrySet()
      .stream()
      .filter(field -> fieldNames.contains(field.getKey()))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return generate(fieldsToTransformIntoLines, anchors);
  }

  /**
   * Generate line items.
   * Use this method if you want to use a single anchor field.
   */
  public static LineItems generate(
      List<String> fieldNames,
      Map<String, ListField> fields,
      Anchor anchor
  ) {
    Map<String, ListField> fieldsToTransformIntoLines = fields
      .entrySet()
      .stream()
      .filter(field -> fieldNames.contains(field.getKey()))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    List<Anchor> anchors = new ArrayList<>(Collections.singletonList(anchor));
    return generate(fieldsToTransformIntoLines, anchors);
  }

  private static LineItems generate(
      Map<String, ListField> fieldsToTransformIntoLines,
      List<Anchor> anchors
  ) {
    PreparedLines preparedLines = LineGenerator.prepareLines(fieldsToTransformIntoLines, anchors);
    List<Line> lines = populateLines(
      fieldsToTransformIntoLines,
      preparedLines.getLines(),
      preparedLines.getAnchor().getTolerance()
    );
    return new LineItems(lines);
  }

  private static List<Line> populateLines(
      Map<String, ListField> fields,
      List<Line> lines,
      double tolerance
  ) {
    List<Line> populatedLines = new ArrayList<>();

    for (Line currentLine : lines) {
      for (Map.Entry<String, ListField> field : fields.entrySet()) {
        for (ListFieldValue listFieldValue : field.getValue().getValues()) {
          MinMax minMaxY = listFieldValue.getPolygon().getMinMaxY();

          if (
            Math.abs(minMaxY.getMax() - currentLine.getBbox().getMaxY()) <= tolerance
              && Math.abs(minMaxY.getMin() - currentLine.getBbox().getMinY()) <= tolerance
          ) {
            currentLine.addField(field.getKey(), listFieldValue);
          }
        }
      }
      populatedLines.add(currentLine);
    }
    return populatedLines;
  }
}
