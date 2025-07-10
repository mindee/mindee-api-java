package com.mindee.parsing.v2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 * Custom deserializer for {@link DynamicField}.
 */
public final class DynamicFieldDeserializer extends JsonDeserializer<DynamicField> {

  @Override
  public DynamicField deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    ObjectCodec codec = jp.getCodec();
    JsonNode root = codec.readTree(jp);

    // -------- LIST FEATURE --------
    if (root.has("items") && root.get("items").isArray()) {
      ListField list = new ListField();
      for (JsonNode itemNode : root.get("items")) {
        list.getItems().add(codec.treeToValue(itemNode, DynamicField.class));
      }
      return DynamicField.of(list);
    }

    // -------- OBJECT WITH NESTED FIELDS --------
    if (root.has("fields") && root.get("fields").isObject()) {
      ObjectField objectField = codec.treeToValue(root, ObjectField.class);
      return DynamicField.of(objectField);
    }

    // -------- SIMPLE OBJECT --------
    if (root.has("value")) {
      SimpleField simple = codec.treeToValue(root, SimpleField.class);
      return DynamicField.of(simple);
    }

    return null;
  }
}
