package com.mindee.parsing.common.field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.Iterator;

/**
 * JSON deserializer for custom documents v1.x.
 */
public class TaxesDeserializer extends StdDeserializer<Taxes> {

  private static ObjectMapper mapper = new ObjectMapper();

  public TaxesDeserializer(Class<?> vc) {
    super(vc);
  }

  public TaxesDeserializer() {
    this(null);
  }

  @Override
  public Taxes deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
    Taxes taxes = new Taxes();
    ArrayNode arrayNode = jsonParser.getCodec().readTree(jsonParser);
    for (Iterator<JsonNode> subnode = arrayNode.iterator(); subnode.hasNext();) {
      JsonNode item = subnode.next();
      TaxField line = mapper.readerFor(new TypeReference<TaxField>() {}).readValue(item);
      taxes.add(line);
    }
    return taxes;
  }
}
