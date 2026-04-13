package com.mindee.v1.parsing.generated;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mindee.v1.product.generated.GeneratedV1Document;
import java.io.IOException;
import java.util.HashMap;

/**
 * JSON deserializer for generated documents v1.x.
 */
public class GeneratedV1PredictionDeserializer extends StdDeserializer<GeneratedV1Document> {

  public GeneratedV1PredictionDeserializer(Class<?> vc) {
    super(vc);
  }

  public GeneratedV1PredictionDeserializer() {
    this(null);
  }

  @Override
  public GeneratedV1Document deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {
    var mapper = jsonParser.getCodec();
    ObjectNode rootNode = mapper.readTree(jsonParser);
    var features = new HashMap<String, GeneratedFeature>();

    for (var featureNode : rootNode.properties()) {
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
