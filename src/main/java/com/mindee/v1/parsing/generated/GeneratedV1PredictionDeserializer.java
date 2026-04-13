package com.mindee.v1.parsing.generated;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mindee.v1.product.generated.GeneratedV1Document;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * JSON deserializer for generated documents v1.x.
 */
public class GeneratedV1PredictionDeserializer extends StdDeserializer<GeneratedV1Document> {

  public GeneratedV1PredictionDeserializer(Class<?> vc) {
    super(vc);
  }

  private static final ObjectMapper mapper = new ObjectMapper();

  public GeneratedV1PredictionDeserializer() {
    this(null);
  }

  @Override
  public GeneratedV1Document deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {
    ObjectNode rootNode = jsonParser.getCodec().readTree(jsonParser);
    Map<String, GeneratedFeature> features = new HashMap<>();

    for (Iterator<Map.Entry<String, JsonNode>> subNode = rootNode.fields(); subNode.hasNext();) {
      Map.Entry<String, JsonNode> featureNode = subNode.next();
      String featureName = featureNode.getKey();

      GeneratedFeature feature;

      if (featureNode.getValue().isArray()) {
        feature = new GeneratedFeature(true);
        for (JsonNode item : featureNode.getValue()) {
          GeneratedObject value = mapper.treeToValue(item, GeneratedObject.class);
          feature.add(value);
        }
        features.put(featureName, feature);
      } else {
        feature = new GeneratedFeature(false);
        GeneratedObject value = mapper.treeToValue(featureNode.getValue(), GeneratedObject.class);
        feature.add(value);
      }
      features.put(featureName, feature);
    }
    return new GeneratedV1Document(features);
  }
}
