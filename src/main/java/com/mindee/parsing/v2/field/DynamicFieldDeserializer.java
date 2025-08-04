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
 * Custom deserializer for {@link DynamicField}.
 */
public final class DynamicFieldDeserializer extends JsonDeserializer<DynamicField> {

  @Override
  public DynamicField deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    ObjectCodec codec = jp.getCodec();
    JsonNode root = codec.readTree(jp);

    if (root.has("items") && root.get("items").isArray()) {
      ListField listField = codec.treeToValue(root, ListField.class);
      return DynamicField.of(listField);
    }

    if (root.has("fields") && root.get("fields").isObject()) {
      ObjectField objectField = codec.treeToValue(root, ObjectField.class);
      return DynamicField.of(objectField);
    }

    if (root.has("value")) {
      SimpleField simple = codec.treeToValue(root, SimpleField.class);
      return DynamicField.of(simple);
    }

    return null;
  }
}
