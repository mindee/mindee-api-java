package com.mindee.model.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mindee.model.documenttype.BaseDocumentResponse;
import com.mindee.model.documenttype.DocumentLevelResponse;
import com.mindee.model.documenttype.PageLevelResponse;
import com.mindee.model.documenttype.PredictionApiResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DocumentResponseDeserializer<T extends BaseDocumentResponse & PredictionApiResponse<S,U>,
  S extends DocumentLevelResponse, U extends PageLevelResponse> extends StdDeserializer<T> {

  private static ObjectMapper MAPPER = new ObjectMapper();
  private Supplier<T> documenResponseSupplier;
  private Class<U> pageClazz;
  private Class<S> documentClazz;


  static {
    MAPPER.registerModule(new JavaTimeModule());
  }

  private DocumentResponseDeserializer(Class<?> vc) {
    super(vc);
  }

  public DocumentResponseDeserializer(Supplier<T> documenResponseSupplier, Class<S> documentClazz,
    Class<U> pageClazz) {
    this(null);
    this.documenResponseSupplier = documenResponseSupplier;
    this.pageClazz = pageClazz;
    this.documentClazz = documentClazz;
  }

  @Override
  public T deserialize(JsonParser jsonParser,
    DeserializationContext deserializationContext) throws IOException, JacksonException {
    T response = documenResponseSupplier.get();
    List<U> pages = new ArrayList<>();

    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    response.setRawResponse(MAPPER.treeToValue(node, Map.class));
    JsonNode documentNode = node.get("document");
    response.setFilename(documentNode.has("name") ? documentNode.get("name").asText():"");
    JsonNode inference = documentNode.get("inference");
    response.setType((inference.has("product") && inference.get("product").has("name"))?inference.get("product").get("name").asText():"");
    JsonNode documentLevelPrediction = inference.get("prediction");
    ArrayNode jsonPages = (ArrayNode) inference.get("pages");
    ArrayNode ocrPages = null;
    if (documentNode.has("ocr") && documentNode.get("ocr").has("mvision-v1")) {
      ocrPages = (ArrayNode) documentNode.get("ocr").get("mvision-v1").get("pages");
    }
    for (JsonNode pageNode : jsonPages) {
      JsonNode predication = pageNode.get("prediction");
      JsonNode pageId = pageNode.get("id");
      int pageNumber = pageId.asInt();
      ((ObjectNode) predication).set("page", pageId);
      if(ocrPages!=null && ocrPages.has(pageNumber))
      ((ObjectNode) predication).set("page_content", ocrPages.get(pageNumber));
      U page = MAPPER.treeToValue(predication,pageClazz);
      pages.add(page);
    }
    response.setPages(pages);


    S document = MAPPER.treeToValue(documentLevelPrediction,documentClazz);
    response.setDocument(document);

    return response;
  }
}
