package com.mindee.model.customdocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mindee.model.geometry.Polygon;
import com.mindee.utils.DeserializationUtils;
import com.mindee.utils.geometry.PolygonUtils;

public class DeserializerUtils {
  private DeserializerUtils() {
  }

  public static ListField getFieldsFromANode(JsonNode fieldPrediction, String fieldName) throws IOException {

    List<ListFieldValue> listFieldValues = new ArrayList<>();

    if (fieldPrediction.has(fieldName)) {
      return null;
    }
      for (JsonNode fieldNode : fieldPrediction.get(fieldName).get("values")) {
        if (fieldNode == null
            || DeserializationUtils.getRawValueFromPrediction(fieldNode, "content") == null) {
          continue;
        }
        ListFieldValue listFieldValue = new ListFieldValue(
            DeserializationUtils.getRawValueFromPrediction(
                fieldNode,
                "content"),
            DeserializationUtils.getConfidenceFromPrediction(fieldNode),
            getPolygonFromPredication(fieldNode));

        listFieldValues.add(listFieldValue);
      }

    return new ListField(
      fieldName,
      DeserializationUtils.getConfidenceFromPrediction(fieldPrediction),
      false,
      DeserializationUtils.getPageIdFromPrediction(fieldPrediction),
      listFieldValues);
  }

  private static Polygon getPolygonFromPredication(JsonNode abstractPrediction)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    if (abstractPrediction.get("polygon") != null) {
      List<List<Double>> coordinates = mapper.readerFor(new TypeReference<List<List<Double>>>() {
      }).readValue(abstractPrediction.get("polygon"));

      return PolygonUtils.getFrom(coordinates);
    }
    return null;
  }
}
