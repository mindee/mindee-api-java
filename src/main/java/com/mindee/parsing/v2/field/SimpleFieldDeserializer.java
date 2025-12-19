package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom deserializer for {@link SimpleField}.
 */
public final class SimpleFieldDeserializer extends JsonDeserializer<SimpleField> {

  @Override
  public SimpleField deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    ObjectCodec codec = jp.getCodec();
    JsonNode root = codec.readTree(jp);

    JsonNode valueNode = root.get("value");
    Object value = null;

    if (valueNode != null && !valueNode.isNull()) {
      switch (valueNode.getNodeType()) {
        case BOOLEAN:
          value = valueNode.booleanValue();
          break;
        case NUMBER:
          value = valueNode.doubleValue();
          break;
        case STRING:
          value = valueNode.textValue();
          break;
        default:
          value = codec.treeToValue(valueNode, Object.class);
      }
    }

    FieldConfidence confidence = codec.treeToValue(root.get("confidence"), FieldConfidence.class);

    List<FieldLocation> locations = null;
    JsonNode locationsNode = root.get("locations");
    if (locationsNode != null && locationsNode.isArray()) {
      locations = new ArrayList<>();
      for (JsonNode node : locationsNode) {
        locations.add(codec.treeToValue(node, FieldLocation.class));
      }
    }

    return new SimpleField(value, confidence, locations);
  }
}
