package com.mindee.parsing.generated;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * JSON deserializer for generated object.
 */
public class GeneratedObjectFieldDeserializer extends StdDeserializer<GeneratedObjectField> {

  public GeneratedObjectFieldDeserializer(Class<?> vc) {
    super(vc);
  }

  public GeneratedObjectFieldDeserializer() {
    this(null);
  }

  @Override
  public GeneratedObjectField deserialize(
    JsonParser jsonParser,
    DeserializationContext deserializationContext
  ) throws IOException {
    ObjectNode node = jsonParser.getCodec().readTree(jsonParser);
    Map<String, Object> values = new HashMap<>();
    ArrayList<String> printableValues = new ArrayList<>();

    for (Iterator<Map.Entry<String, JsonNode>> subNode = node.fields(); subNode.hasNext(); ) {

      Map.Entry<String, JsonNode> generatedObjectNode = subNode.next();
      JsonNode valueNode = generatedObjectNode.getValue();

      if (Arrays.asList(new String[] {"polygon", "rectangle", "quadrangle", "bounding_box"})
        .contains(generatedObjectNode.getKey())) {
        printableValues.add(generatedObjectNode.getKey());
      } else if (!Arrays.asList(new String[] {"page_id", "confidence", "raw_value"})
        .contains(generatedObjectNode.getKey())) {
        printableValues.add(generatedObjectNode.getKey());
        String value;
        if (valueNode.isNumber()) {
          double numericValue = valueNode.asDouble();
          if ((numericValue == Math.floor(numericValue)) && !Double.isInfinite(numericValue) &&
            numericValue != 0.0) {
            value = numericValue + ".0";
          } else {
            value = String.valueOf(numericValue);
          }
        } else if (valueNode.isNull()) {
          value = null;
        } else {
          value = valueNode.asText();
        }

        values.put(
          generatedObjectNode.getKey(),
          value
        );
      }
    }

    return new
      GeneratedObjectField(values, printableValues);
  }
}
