package com.mindee.model.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mindee.model.geometry.Polygon;
import com.mindee.utils.geometry.PolygonUtils;
import java.io.IOException;
import java.util.List;

public class PolygonDeserializer extends StdDeserializer<Polygon> {


  private static ObjectMapper MAPPER = new ObjectMapper();
  public PolygonDeserializer(Class<?> vc) {
    super(vc);
  }

  public PolygonDeserializer() {
    this(null);
  }

  @Override
  public Polygon deserialize(JsonParser jsonParser,
    DeserializationContext deserializationContext) throws IOException, JacksonException {
    ArrayNode node = jsonParser.getCodec().readTree(jsonParser);

    List<List<Double>> polygonList = MAPPER.readerFor(new TypeReference<List<List<Double>>>() {
    }).readValue(node);

    return PolygonUtils.getFrom(polygonList);
  }
}
