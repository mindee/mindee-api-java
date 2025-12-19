package com.mindee.geometry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.List;

/**
 * JSON deserializer for polygons.
 */
public class PolygonDeserializer extends StdDeserializer<Polygon> {

  private static final ObjectMapper mapper = new ObjectMapper();

  public PolygonDeserializer(Class<?> vc) {
    super(vc);
  }

  public PolygonDeserializer() {
    this(null);
  }

  @Override
  public Polygon deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {
    ArrayNode node = jsonParser.getCodec().readTree(jsonParser);

    TypeReference<List<List<Double>>> typeRef = new TypeReference<List<List<Double>>>() {
    };
    List<List<Double>> polygonList = mapper.readerFor(typeRef).readValue(node);

    return PolygonUtils.getFrom(polygonList);
  }
}
