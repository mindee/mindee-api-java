package com.mindee.parsing.generated;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GeneratedHelper {


  // Method to check if a JsonNode contains any of the specified keys at the immediate level
  public static boolean containsImmediateKey(JsonNode node, List<String> keysToCheck) {
    if (node.isObject()) {
      Iterator<String> fieldNames = node.fieldNames();
      while (fieldNames.hasNext()) {
        String key = fieldNames.next();
        if (keysToCheck.contains(key)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Checks whether a field is a custom object or not.
   *
   * @param strDict Input JsonNode to check.
   * @return boolean Whether the field is a custom object.
   */
  public static boolean isGeneratedObject(JsonNode strDict) {
    List<String> commonKeys = Arrays.asList(
      "value",
      "polygon",
      "rectangle",
      "page_id",
      "confidence",
      "quadrangle",
      "values",
      "raw_value"
    );
    Iterator<String> fieldNames = strDict.fieldNames();
    while (fieldNames.hasNext()) {
      String key = fieldNames.next();
      if (!commonKeys.contains(key)) {
        return true;
      }
    }
    return false;
  }
}
