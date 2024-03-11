package com.mindee.parsing.generated;

import com.mindee.product.generated.GeneratedV1Document;
import com.mindee.product.generated.GeneratedV1Page;
import java.lang.reflect.ParameterizedType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mindee.geometry.Point;
import com.mindee.geometry.Polygon;
import com.mindee.parsing.standard.StringField;
import com.mindee.product.generated.GeneratedV1Prediction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GeneratedV1PredictionDeserializer
  extends StdDeserializer<GeneratedV1Prediction> {
  private final Class<? extends GeneratedV1Prediction> targetType;

  public GeneratedV1PredictionDeserializer(Class<?> vc) {
    super(vc);
    targetType = extractTargetType();
  }

  public GeneratedV1PredictionDeserializer() {
    this(null);
  }

  @Override
  public GeneratedV1Prediction deserialize(JsonParser jsonParser,
                                           DeserializationContext deserializationContext) throws
    IOException {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode rootNode = jsonParser.readValueAsTree();

    Map<String, Object> fields = new HashMap<>();
    for (
      Iterator<Map.Entry<String, JsonNode>> subNode = rootNode.fields(); subNode.hasNext(); ) {

      Map.Entry<String, JsonNode> objectNode = subNode.next();
      String fieldName = objectNode.getKey();
      JsonNode fieldValue = objectNode.getValue();
      if (fieldValue.isArray()) {
        fields.put(fieldName,
          mapper.readerFor(new TypeReference<GeneratedListField>() {
          }).readValue(fieldValue));
      } else if (GeneratedObjectField.isGeneratedObject(fieldValue)) {
        fields.put(fieldName,
          mapper.readerFor(new TypeReference<GeneratedObjectField>() {
          }).readValue(fieldValue));
      } else {
        if (fieldValue.has("value")) {
          String value =
            fieldValue.has("value") && fieldValue.get("value") != null && !fieldValue.isNull() ?
              fieldValue.asText() : null;
          Double confidence =
            fieldValue.has("confidence") && fieldValue.get("confidence") != null &&
              fieldValue.isDouble() ? fieldValue.asDouble() : null;
          List<Point> polygonPoints = new ArrayList<>();
          if (fieldValue.has("polygon") && fieldValue.get("polygon") != null) {
            for (JsonNode pointNode : fieldValue.get("polygon")) {
              if (pointNode != null && pointNode.isArray() && pointNode.size() >= 2) {
                double x = pointNode.get(0).asDouble();
                double y = pointNode.get(1).asDouble();

                polygonPoints.add(new Point(x, y));
              }
            }
          }
          fields.put(fieldName, new StringField(value, confidence, new Polygon(polygonPoints)));
        }
      }
    }

    if (targetType.equals(GeneratedV1Document.class)) {
      return new GeneratedV1Document(fields);
    } else if (targetType.equals(GeneratedV1Page.class)) {
      return new GeneratedV1Page(fields);
    } else {
      // Handle other cases or throw an exception
      throw new RuntimeException(
        "Cannot use specified specified generic type '" + targetType.getName() +
          "' during GeneratedV1Prediction deserialization.");
    }
  }

  // Method to extract the target type from the generic parameter
  @SuppressWarnings("unchecked")
  private Class<? extends GeneratedV1Prediction> extractTargetType() {
    ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
    return (Class<? extends GeneratedV1Prediction>) superclass.getActualTypeArguments()[0];
  }
}
