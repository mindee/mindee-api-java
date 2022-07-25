package com.mindee.model.deserialization;

import static com.mindee.utils.DeserializationUtils.amountFromJsonNode;
import static com.mindee.utils.DeserializationUtils.dateFromJsonNode;
import static com.mindee.utils.DeserializationUtils.fieldFromJsonNode;
import static com.mindee.utils.DeserializationUtils.getPageContentsFromOcr;
import static com.mindee.utils.DeserializationUtils.localeFromJsonNode;
import static com.mindee.utils.DeserializationUtils.orientationFromJsonNode;
import static com.mindee.utils.DeserializationUtils.paymentDetailsJsonNode;
import static com.mindee.utils.DeserializationUtils.taxFromJsonNode;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mindee.model.documenttype.InvoiceResponse;
import com.mindee.model.documenttype.InvoiceResponse.InvoiceDocument;
import com.mindee.model.documenttype.InvoiceResponse.InvoicePage;
import com.mindee.model.fields.Amount;
import com.mindee.model.fields.Field;
import com.mindee.model.fields.PaymentDetails;
import com.mindee.model.fields.Tax;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvoiceDeserializer extends StdDeserializer<InvoiceResponse> {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  public InvoiceDeserializer(Class<?> vc) {
    super(vc);
  }

  public InvoiceDeserializer() {
    this(null);
  }

  @Override
  public InvoiceResponse deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext)
      throws IOException, JacksonException {
    InvoiceResponse invoiceResponse = new InvoiceResponse();
    invoiceResponse.setType("invoice");
    List<InvoicePage> pages = new ArrayList<>();

    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    invoiceResponse.setRawResponse(MAPPER.treeToValue(node, Map.class));
    JsonNode documentNode = node.get("document");
    invoiceResponse.setFilename(documentNode.get("name").asText());
    JsonNode inference = documentNode.get("inference");
    ArrayNode ocrPages = null;
    if (documentNode.has("ocr")) {
      ocrPages = (ArrayNode) documentNode.get("ocr").get("mvision-v1").get("pages");
    }
    JsonNode documentLevelPrediction = inference.get("prediction");
    ArrayNode jsonPages = (ArrayNode) inference.get("pages");

    for (JsonNode pageNode : jsonPages) {

      JsonNode predication = pageNode.get("prediction");
      ArrayNode paymentDetailsNodes = (ArrayNode) predication.get("payment_details");
      List<PaymentDetails> paymentDetailsList = new ArrayList<>();
      for (JsonNode paymentDetails : paymentDetailsNodes) {
        paymentDetailsList.add(paymentDetailsJsonNode(paymentDetails, "iban", "account_number",
            "iban", "routing_number", "swift"));
      }
      ArrayNode companyRegistrationNodes = (ArrayNode) predication.get("company_registration");
      List<Field> companyRegistrationList = new ArrayList<>();
      for (JsonNode companyRegistration : companyRegistrationNodes) {
        companyRegistrationList.add(
            fieldFromJsonNode(companyRegistration, "value", new String[]{"type"}));
      }
      ArrayNode taxNodes = (ArrayNode) predication.get("taxes");
      List<Tax> pageLevelTaxes = new ArrayList<>();
      for (JsonNode tax : taxNodes) {
        pageLevelTaxes.add(taxFromJsonNode(tax, "value", "rate", "code"));
      }
      ArrayNode customerCompanyRegistrationNodes = (ArrayNode) predication.get(
          "customer_company_registration");
      List<Field> pageLevelCustomerCompanyRegistrationList = new ArrayList<>();
      for (JsonNode customerCompanyRegistration : customerCompanyRegistrationNodes) {
        pageLevelCustomerCompanyRegistrationList.add(
            fieldFromJsonNode(customerCompanyRegistration, "value", new String[]{"type"}));
      }

      InvoicePage page = InvoicePage.builder()
          .page(pageNode.get("id").asInt())
          .companyNumber(companyRegistrationList)
          .locale(localeFromJsonNode(predication.get("locale"), "language"))
          .totalIncl(amountFromJsonNode(predication.get("total_incl")))
          .invoiceDate(dateFromJsonNode(predication.get("date")))
          .dueDate(dateFromJsonNode(predication.get("due_date")))
          .invoiceNumber(fieldFromJsonNode(predication.get("invoice_number")))
          .supplier(fieldFromJsonNode(predication.get("supplier")))
          .supplierAddress(fieldFromJsonNode(predication.get("supplier_address")))
          .customerName(fieldFromJsonNode(predication.get("customer")))
          .customerAddress(fieldFromJsonNode(predication.get("customer_address")))
          .customerCompanyRegistration(pageLevelCustomerCompanyRegistrationList)
          .paymentDetails(paymentDetailsList)
          .orientation(orientationFromJsonNode(predication.get("orientation"), "degrees"))
          .taxes(pageLevelTaxes)
          .fullText(
              getPageContentsFromOcr(ocrPages, pageNode.get("id").asInt(), "all_words", "text"))
          .totalExcl(amountFromJsonNode(predication.get("total_excl")))
          .totalTax(Amount.builder()
              .confidence(0.0)
              .reconstructed(false)
              .build())
          .build();

      pages.add(page);
    }

    ArrayNode paymentDetailsNodes = (ArrayNode) documentLevelPrediction.get("payment_details");
    List<PaymentDetails> paymentDetailsList = new ArrayList<>();
    for (JsonNode paymentDetails : paymentDetailsNodes) {
      paymentDetailsList.add(paymentDetailsJsonNode(paymentDetails, "iban", "account_number",
          "iban", "routing_number", "swift"));
    }
    ArrayNode companyRegistrationNodes = (ArrayNode) documentLevelPrediction.get(
        "company_registration");
    List<Field> companyRegistrationList = new ArrayList<>();
    for (JsonNode companyRegistration : companyRegistrationNodes) {
      companyRegistrationList.add(
          fieldFromJsonNode(companyRegistration, "value", new String[]{"type"}));
    }
    ArrayNode taxNodes = (ArrayNode) documentLevelPrediction.get("taxes");
    List<Tax> pageLevelTaxes = new ArrayList<>();
    for (JsonNode tax : taxNodes) {
      pageLevelTaxes.add(taxFromJsonNode(tax, "value", "rate", "code"));
    }
    ArrayNode customerCompanyRegistrationNodes = (ArrayNode) documentLevelPrediction.get(
        "customer_company_registration");
    List<Field> docLevelCustomerCompanyRegistrationList = new ArrayList<>();
    for (JsonNode customerCompanyRegistration : customerCompanyRegistrationNodes) {
      docLevelCustomerCompanyRegistrationList.add(
          fieldFromJsonNode(customerCompanyRegistration, "value", new String[]{"type"}));
    }
    InvoiceDocument document = InvoiceDocument.builder()
        .companyNumber(companyRegistrationList)
        .locale(localeFromJsonNode(documentLevelPrediction.get("locale"), "language"))
        .totalIncl(amountFromJsonNode(documentLevelPrediction.get("total_incl")))
        .invoiceDate(dateFromJsonNode(documentLevelPrediction.get("date")))
        .dueDate(dateFromJsonNode(documentLevelPrediction.get("due_date")))
        .invoiceNumber(fieldFromJsonNode(documentLevelPrediction.get("invoice_number")))
        .supplier(fieldFromJsonNode(documentLevelPrediction.get("supplier")))
        .supplierAddress(fieldFromJsonNode(documentLevelPrediction.get("supplier_address")))
        .customerName(fieldFromJsonNode(documentLevelPrediction.get("customer")))
        .customerAddress(fieldFromJsonNode(documentLevelPrediction.get("customer_address")))
        .customerCompanyRegistration(docLevelCustomerCompanyRegistrationList)
        .paymentDetails(paymentDetailsList)
        .taxes(pageLevelTaxes)
        .totalExcl(amountFromJsonNode(documentLevelPrediction.get("total_excl")))
        .totalTax(Amount.builder()
            .confidence(0.0)
            .reconstructed(false)
            .build())
        .build();

    invoiceResponse.setType("invoice");
    invoiceResponse.setInvoice(document);
    invoiceResponse.setInvoices(pages);

    return invoiceResponse;
  }


}
