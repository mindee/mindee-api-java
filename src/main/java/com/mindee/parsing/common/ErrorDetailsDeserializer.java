package com.mindee.parsing.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

public class ErrorDetailsDeserializer extends StdDeserializer<ErrorDetails> {

  public ErrorDetailsDeserializer(Class<?> vc) {
    super(vc);
  }

  public ErrorDetailsDeserializer() {
    this(null);
  }

  @Override
  public ErrorDetails deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {

    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    String details;

    if (node.isObject()) {
      details = node.toString();
    } else if (node.isTextual()) {
      details = node.textValue();
    } else {
      throw new IllegalStateException("The JSON type is not handled.");
    }

    return new ErrorDetails(details);
  }
}
