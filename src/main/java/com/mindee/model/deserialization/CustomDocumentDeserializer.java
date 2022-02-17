package com.mindee.model.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.mindee.model.documenttype.CustomDocumentResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomDocumentDeserializer extends StdDeserializer<CustomDocumentResponse> {

  private static final ObjectMapper mapper = new ObjectMapper();
  private final String singular;
  private final String plural;

  public CustomDocumentDeserializer(Class<?> vc, String singular, String plural) {
    super(vc);
    this.singular = singular;
    this.plural = plural;
  }

  public CustomDocumentDeserializer(String singular, String plural) {
    this(null, singular, plural);
  }


  @Override
  public CustomDocumentResponse deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext) throws IOException, JacksonException {
    Map results = mapper.readValue(jsonParser, Map.class);
    Map<String, Object> fields = new HashMap<>();
    Map<String, Object> inference = (Map) ((Map) results.get("document")).get("inference");
    Map<String, Map> document = (Map) inference.get("prediction");
    fields.put(this.singular, document);

    List<Map<String, Object>> pageNodes = (List) inference.get("pages");
    List<Map<String, Object>> pages = new ArrayList<>();
    for (Map<String, Object> page : pageNodes) {
      Map<String, Object> prediction = (Map) page.get("prediction");
      prediction.put("page", page.get("id"));
      pages.add(prediction);
    }

    fields.put(this.plural, pages);
    CustomDocumentResponse response = new CustomDocumentResponse(fields);

    return response;
  }
}
