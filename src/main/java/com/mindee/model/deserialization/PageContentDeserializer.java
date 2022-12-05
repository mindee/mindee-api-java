package com.mindee.model.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mindee.model.ocr.PageContent;
import com.mindee.utils.DeserializationUtils;
import java.io.IOException;

public class PageContentDeserializer extends StdDeserializer<PageContent> {


  private static ObjectMapper MAPPER = new ObjectMapper();
  public PageContentDeserializer(Class<?> vc) {
    super(vc);
  }

  public PageContentDeserializer() {
    this(null);
  }

  @Override
  public PageContent deserialize(JsonParser jsonParser,
    DeserializationContext deserializationContext) throws IOException, JacksonException {
    ArrayNode node = jsonParser.getCodec().readTree(jsonParser);

    return PageContent.builder()
      .words(DeserializationUtils.getAllWordsOnPageFromOcrJsonNode(node, "all_words", "text"))
      .build();
  }
}
