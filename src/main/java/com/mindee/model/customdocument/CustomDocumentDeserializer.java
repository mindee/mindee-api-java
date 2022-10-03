package com.mindee.model.customdocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomDocumentDeserializer
    extends StdDeserializer<CustomDocumentResponse> {

  public CustomDocumentDeserializer(Class<?> vc) {
    super(vc);
  }

  public CustomDocumentDeserializer() {
    this(null);
  }

  @Override
  public CustomDocumentResponse deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext)
      throws IOException {

    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    JsonNode inference = node.get("document").get("inference");
    JsonNode prediction = inference.get("prediction");

    List<String> fieldNames = new ArrayList<>();
    Iterator<Entry<String, JsonNode>> apiFields = prediction.fields();
    apiFields.forEachRemaining(field -> {
      fieldNames.add(field.getKey());
    });

    Map<String, ListField> fields = new HashMap<>();

    for (String fieldName : fieldNames) {

      fields.put(fieldName,
          DeserializerUtils.getFieldsFromANode(prediction, fieldName));
    }

    return new CustomDocumentResponse(fields);
  }
}
