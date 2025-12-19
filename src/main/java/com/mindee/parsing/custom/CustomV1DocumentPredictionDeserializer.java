package com.mindee.parsing.custom;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mindee.product.custom.CustomV1Document;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * JSON deserializer for custom documents v1.x.
 */
public class CustomV1DocumentPredictionDeserializer extends StdDeserializer<CustomV1Document> {

  private static final ObjectMapper mapper = new ObjectMapper();

  public CustomV1DocumentPredictionDeserializer(Class<?> vc) {
    super(vc);
  }

  public CustomV1DocumentPredictionDeserializer() {
    this(null);
  }

  @Override
  public CustomV1Document deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {
    ObjectNode node = jsonParser.getCodec().readTree(jsonParser);

    Map<String, ClassificationField> classificationFields = new HashMap<>();
    Map<String, ListField> fields = new HashMap<>();

    for (Iterator<Map.Entry<String, JsonNode>> subNode = node.fields(); subNode.hasNext();) {

      Map.Entry<String, JsonNode> pageNode = subNode.next();

      if (pageNode.getValue().has("value")) {
        classificationFields
          .put(pageNode.getKey(), mapper.readerFor(new TypeReference<ClassificationField>() {
          }).readValue(pageNode.getValue()));
      } else {
        fields.put(pageNode.getKey(), mapper.readerFor(new TypeReference<ListField>() {
        }).readValue(pageNode.getValue()));
      }
    }

    return new CustomV1Document(classificationFields, fields);
  }
}
