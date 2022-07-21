package com.mindee.model.deserialization;

import static com.mindee.utils.DeserializationUtils.amountFromJsonNode;
import static com.mindee.utils.DeserializationUtils.dateFromJsonNode;
import static com.mindee.utils.DeserializationUtils.fieldFromJsonNode;
import static com.mindee.utils.DeserializationUtils.getPageContentsFromOcr;
import static com.mindee.utils.DeserializationUtils.localeFromJsonNode;
import static com.mindee.utils.DeserializationUtils.orientationFromJsonNode;
import static com.mindee.utils.DeserializationUtils.taxFromJsonNode;
import static com.mindee.utils.DeserializationUtils.timeFromJsonNode;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mindee.model.documenttype.ReceiptResponse;
import com.mindee.model.documenttype.ReceiptResponse.ReceiptDocument;
import com.mindee.model.documenttype.ReceiptResponse.ReceiptPage;
import com.mindee.model.fields.Amount;
import com.mindee.model.fields.Tax;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReceiptDeserializer extends StdDeserializer<ReceiptResponse> {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  public ReceiptDeserializer(Class<?> vc) {
    super(vc);
  }

  public ReceiptDeserializer() {
    this(null);
  }

  @Override
  public ReceiptResponse deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext)
      throws IOException, JacksonException {

    ReceiptResponse receiptResponse = new ReceiptResponse();
    receiptResponse.setType("receipt");
    List<ReceiptPage> pages = new ArrayList<>();

    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    receiptResponse.setRawResponse(MAPPER.treeToValue(node, Map.class));
    JsonNode documentNode = node.get("document");
    receiptResponse.setFilename(documentNode.get("name").asText());
    JsonNode inference = documentNode.get("inference");
    JsonNode documentLevelPrediction = inference.get("prediction");
    ArrayNode jsonPages = (ArrayNode) inference.get("pages");
    ArrayNode ocrPages = null;
    if (documentNode.has("ocr")) {
      ocrPages = (ArrayNode) documentNode.get("ocr").get("mvision-v1").get("pages");
    }
    for (JsonNode pageNode : jsonPages) {

      JsonNode predication = pageNode.get("prediction");
      ArrayNode taxNodes = (ArrayNode) predication.get("taxes");
      List<Tax> pageLevelTaxes = new ArrayList<>();
      for (JsonNode tax : taxNodes) {
        pageLevelTaxes.add(taxFromJsonNode(tax, "value", "rate", "code"));
      }
      ReceiptPage page = ReceiptPage.builder()
          .page(pageNode.get("id").asInt())
          .locale(localeFromJsonNode(predication.get("locale")))
          .totalIncl(amountFromJsonNode(predication.get("total_incl")))
          .date(dateFromJsonNode(predication.get("date")))
          .category(fieldFromJsonNode(predication.get("category")))
          .merchantName(fieldFromJsonNode(predication.get("supplier")))
          .time(timeFromJsonNode(predication.get("time")))
          .orientation(orientationFromJsonNode(predication.get("orientation"), "degrees"))
          .taxes(pageLevelTaxes)
          .fullText(
              getPageContentsFromOcr(ocrPages, pageNode.get("id").asInt(), "all_words", "text"))
          .totalExcl(Amount.builder()
              .confidence(0.0)
              .reconstructed(false)
              .build())
          .totalTax(Amount.builder()
              .confidence(0.0)
              .reconstructed(false)
              .build())
          .build();

      pages.add(page);
    }
    ArrayNode taxNodes = (ArrayNode) documentLevelPrediction.get("taxes");
    List<Tax> documentLevelTaxes = new ArrayList<>();
    for (JsonNode tax : taxNodes) {
      documentLevelTaxes.add(taxFromJsonNode(tax, "value", "rate", "code"));
    }
    ReceiptDocument document = ReceiptDocument.builder()
        .locale(localeFromJsonNode(documentLevelPrediction.get("locale")))
        .totalIncl(amountFromJsonNode(documentLevelPrediction.get("total_incl")))
        .date(dateFromJsonNode(documentLevelPrediction.get("date")))
        .category(fieldFromJsonNode(documentLevelPrediction.get("category")))
        .merchantName(fieldFromJsonNode(documentLevelPrediction.get("supplier")))
        .time(timeFromJsonNode(documentLevelPrediction.get("time")))
        .taxes(documentLevelTaxes)
        .totalExcl(Amount.builder()
            .confidence(0.0)
            .reconstructed(false)
            .build())
        .totalTax(Amount.builder()
            .confidence(0.0)
            .reconstructed(false)
            .build())
        .build();

    receiptResponse.setReceipt(document);
    receiptResponse.setReceipts(pages);

    return receiptResponse;
  }

}
