package com.mindee.v2.product.extraction.ragdocuments;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 * Custom deserializer for {@link AnnotatedSimpleField}.
 */
public final class AnnotatedSimpleFieldDeserializer extends JsonDeserializer<AnnotatedSimpleField> {

  @Override
  public AnnotatedSimpleField deserialize(
      JsonParser jp,
      DeserializationContext ctxt
  ) throws IOException {
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

    boolean selected = false;
    JsonNode selectedNode = root.get("selected");
    if (selectedNode != null && !selectedNode.isNull()) {
      selected = selectedNode.booleanValue();
    }

    String guidelines = null;
    JsonNode guidelinesNode = root.get("guidelines");
    if (guidelinesNode != null && !guidelinesNode.isNull()) {
      guidelines = guidelinesNode.textValue();
    }

    return new AnnotatedSimpleField(value, selected, guidelines);
  }
}
