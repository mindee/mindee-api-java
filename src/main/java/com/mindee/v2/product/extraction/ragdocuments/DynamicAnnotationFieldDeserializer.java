package com.mindee.v2.product.extraction.ragdocuments;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Custom deserializer for {@link AnnotatedDynamicField}.
 */
public final class DynamicAnnotationFieldDeserializer
    extends JsonDeserializer<AnnotatedDynamicField> {

  @Override
  public AnnotatedDynamicField deserialize(
      JsonParser jp,
      DeserializationContext ctxt
  ) throws IOException {
    ObjectCodec codec = jp.getCodec();
    JsonNode root = codec.readTree(jp);

    if (root == null || root.isNull()) {
      return null;
    }

    // -------- LIST OF FIELDS --------
    JsonNode itemsNode = root.get("items");
    if (itemsNode != null && itemsNode.isArray()) {
      String guidelines = null;
      JsonNode guidelinesNode = root.get("guidelines");
      if (guidelinesNode != null && !guidelinesNode.isNull()) {
        guidelines = guidelinesNode.textValue();
      }

      boolean selected = false;
      JsonNode selectedNode = root.get("selected");
      if (selectedNode != null && !selectedNode.isNull()) {
        selected = selectedNode.booleanValue();
      }

      var items = new ArrayList<AnnotatedDynamicField>();
      for (JsonNode item : itemsNode) {
        items.add(codec.treeToValue(item, AnnotatedDynamicField.class));
      }
      AnnotatedListField listField = new AnnotatedListField(items, selected, guidelines);

      return new AnnotatedDynamicField(listField);
    }

    // -------- OBJECT FIELD --------
    JsonNode fieldsNode = root.get("fields");
    if (fieldsNode != null && fieldsNode.isObject()) {
      AnnotatedObjectField objectField = codec.treeToValue(root, AnnotatedObjectField.class);
      return new AnnotatedDynamicField(objectField);
    }

    // -------- SIMPLE FIELD --------
    if (root.has("value")) {
      AnnotatedSimpleField simpleField = codec.treeToValue(root, AnnotatedSimpleField.class);
      return new AnnotatedDynamicField(simpleField);
    }

    throw new IOException("Unknown field: " + root.toString());
  }
}
