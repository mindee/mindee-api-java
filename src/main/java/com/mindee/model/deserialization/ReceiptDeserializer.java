package com.mindee.model.deserialization;

import static com.mindee.utils.DeserializationUtils.amountFromJsonNode;
import static com.mindee.utils.DeserializationUtils.dateFromJsonNode;
import static com.mindee.utils.DeserializationUtils.fieldFromJsonNode;
import static com.mindee.utils.DeserializationUtils.localeFromJsonNode;
import static com.mindee.utils.DeserializationUtils.orientationFromJsonNode;
import static com.mindee.utils.DeserializationUtils.taxFromJsonNode;
import static com.mindee.utils.DeserializationUtils.timeFromJsonNode;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
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

public class ReceiptDeserializer extends StdDeserializer<ReceiptResponse> {

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
    JsonNode inference = node.get("document").get("inference");
    JsonNode documentLevelPrediction = inference.get("prediction");
    ArrayNode jsonPages = (ArrayNode) inference.get("pages");
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

  /*
  private void buildBaseReceiptFromApiPrediction(JsonNode predication, BaseReceipt receipt)
      throws IOException {
    receipt.setLocale(localeFromJsonNode(predication.get("locale")));
    receipt.setTotalIncl(amountFromJsonNode(predication.get("total_incl")));
    receipt.setDate(dateFromJsonNode(predication.get("date")));
    receipt.setCategory(fieldFromJsonNode(predication.get("category")));
    receipt.setMerchantName(fieldFromJsonNode(predication.get("supplier")));
    receipt.setTime(timeFromJsonNode(predication.get("time")));
    ArrayNode taxNodes = (ArrayNode) predication.get("taxes");
    List<Tax> taxes = new ArrayList<>();
    for (JsonNode tax:taxNodes) {
      taxes.add(taxFromJsonNode(tax,"value","rate","code"));
    }
    receipt.setTaxes(taxes);
    if(predication.get("orientation")!=null)
      receipt.setOrientation(orientationFromJsonNode(predication.get("orientation"),"degrees"));
    receipt.setTotalExcl(Amount.builder()
        .confidence(0.0)
        .reconstructed(false)
        .build());
    receipt.setTotalTax(Amount.builder()
            .confidence(0.0)
            .reconstructed(false)
        .build());

  }*/
}
