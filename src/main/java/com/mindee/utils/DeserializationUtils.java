package com.mindee.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mindee.model.fields.Field;
import com.mindee.model.geometry.Polygon;
import com.mindee.utils.geometry.PolygonUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;

public class DeserializationUtils {

  private static ObjectMapper mapper = new ObjectMapper();



  @SneakyThrows
  public static Field fieldFromJsonNode(JsonNode fieldPrediction) {
    return fieldFromJsonNode(fieldPrediction, "value", null);
  }

  @SneakyThrows
  public static Field fieldFromJsonNode(JsonNode fieldPrediction, String valueKey,
      String[] extraFields) {
    if (fieldPrediction == null || getRawValueFromPrediction(fieldPrediction, valueKey) == null) {
      return null;
    }
    Field.FieldBuilder fieldBuilder = Field.builder();
    if (extraFields != null) {
      for (String field : extraFields) {
        fieldBuilder.extraField(field, fieldPrediction.get(field).asText());
      }
    }

    Field field = fieldBuilder
        .confidence(getConfidenceFromPrediction(fieldPrediction))
        .polygon(getPolygonFromPredication(fieldPrediction))
        .page(getPageIdFromPrediction(fieldPrediction))
        .reconstructed(false)
        .value(fieldPrediction.get(valueKey).asText())
        .build();

    return field;
  }




  @SneakyThrows
  public static List<Field> getAllWordsOnPageFromOcrJsonNode(JsonNode ocrPage, String wordsKey,
      String valueKey) {

    ArrayNode words = (ArrayNode) ocrPage.get(wordsKey);
    List<Field> pageWords = new ArrayList<>();
    for (JsonNode word : words) {
      Field wordAsField = fieldFromJsonNode(word, valueKey, null);
      pageWords.add(wordAsField);
    }

    return pageWords;

  }



  public static String getRawValueFromPrediction(JsonNode abstractPrediction, String valueKey) {
    if (abstractPrediction.get(valueKey) != null
        && !abstractPrediction.get(valueKey).isNull()
        && abstractPrediction.get(valueKey).asText() != null
        && !abstractPrediction.get(valueKey).asText().equalsIgnoreCase("N/A")) {
      return abstractPrediction.get(valueKey).asText();
    }
    return null;
  }

  public static Double getConfidenceFromPrediction(JsonNode abstractPrediction) {
    try {
      return abstractPrediction.get("confidence").asDouble();
    } catch (Exception e) {
      return 0.0;
    }

  }

  @SneakyThrows
  public static Integer getPageIdFromPrediction(JsonNode abstractPrediction)  {
    if (abstractPrediction.get("page_id") != null) {
      return abstractPrediction.get("page_id").asInt();
    } else {
      return null;
    }
  }

  @SneakyThrows
  public static Polygon getPolygonFromPredication(JsonNode abstractPrediction)
       {
    if (abstractPrediction.get("polygon") != null) {
      List<List<Double>> polygonList = mapper.readerFor(new TypeReference<List<List<Double>>>() {
      }).readValue(abstractPrediction.get("polygon"));

      return PolygonUtils.getFrom(polygonList);
    }
    return null;
  }





}
