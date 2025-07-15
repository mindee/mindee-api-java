package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

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
      if (valueNode.isTextual()) {
        value = valueNode.asText();
      } else if (valueNode.isNumber()) {
        value = valueNode.doubleValue();
      } else if (valueNode.isBoolean()) {
        value = valueNode.asBoolean();
      }
    }
    return new SimpleField(value);
  }
}
