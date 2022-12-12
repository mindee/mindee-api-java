package com.mindee.documentparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mindee.http.Endpoint;
import com.mindee.model.deserialization.DocumentResponseDeserializerFactory;
import com.mindee.model.documenttype.*;
import com.mindee.model.documenttype.invoice.InvoiceV4Response;
import com.mindee.model.postprocessing.PassportResponsePostProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

final class DocumentConfigFactory {

  private static final String API_TYPE_CUSTOM = "api_builder";
  private static final String API_TYPE_OFF_THE_SHELF = "off_the_shelf";
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String RECEIPT = "receipt";
  private static final String INVOICE = "invoice";
  private static final String FINANCIAL_DOCUMENT = "financial_doc";
  private static final String PASSPORT = "passport";
  private static final String ENV_API_KEY = "MINDEE_API_KEY";
  private static final String MINDEE = "mindee";


  // As we migrate to version based classes, add these to the list
  private static final List<Class> VERSION_BASED_RESPONSE_TYPES = Arrays.asList(
    ReceiptV3Response.class,
    ReceiptV4Response.class,
    InvoiceV3Response.class,
    InvoiceV4Response.class,
    PassportV1Response.class
  );

  // Off the shelf response types
  private static final List<Class<? extends BaseDocumentResponse>> RESPONSE_CLASS_TYPES = Arrays.asList(
    ReceiptV3Response.class,
    ReceiptV4Response.class,
    InvoiceV3Response.class,
    InvoiceV4Response.class,
    PassportV1Response.class,
    FinancialDocumentResponse.class
  );

  static {
    SimpleModule module = new SimpleModule();
    for (Class clazz : VERSION_BASED_RESPONSE_TYPES) {
      module.addDeserializer(clazz, DocumentResponseDeserializerFactory.documentResponseDeserializerFromResponseClass(clazz));
    }
    OBJECT_MAPPER.registerModule(module);
  }

  private DocumentConfigFactory() {
  }

  static String getApiKeyFromEnvironmentVariable() {
    return System.getenv(ENV_API_KEY);
  }

  static List<Class<? extends BaseDocumentResponse>> offTheShelfResponseTypes() {
    return RESPONSE_CLASS_TYPES;
  }

  static <T extends BaseDocumentResponse> DocumentConfig<T> getDocumentConfigFromApiKey(
    String apiKey, String docType, String accountName) {
    if (apiKey == null || apiKey.trim().length() == 0) {
      throw new IllegalArgumentException("API KEY is null or blank");
    }

    if (docType == null) {
      throw new IllegalArgumentException("doc type cannot be blank");
    }

    return getDocumentConfigForCustomDocType(docType, accountName, apiKey, "1");

  }

  private static <T extends BaseDocumentResponse> DocumentConfig<T> getDocumentConfigForCustomDocType(
    String docType,
    String owner, String apiKey, String version) {
    if (docType == null || owner == null || apiKey == null || version == null) {
      throw new IllegalArgumentException(
        "DocType, Owner, ApiKey, or Version, cannot be null");
    }


    return DocumentConfig.<T>builder()
      .apiType(API_TYPE_CUSTOM)
      .documentType(docType)
      .converter((clazz, map) -> OBJECT_MAPPER.convertValue(map, clazz))
      .builtInPostProcessing(UnaryOperator.identity())
      .endpoint(Endpoint.builder()
        .apiKey(apiKey)
        .owner(owner)
        .version(version)
        .urlName(docType)
        .keyName(docType)
        .build())
      .build();
  }


  static <T extends BaseDocumentResponse> DocumentConfig getDocumentConfigForOffTheShelfDocType(Class<T> responseClassType, String apiKey) {


    if (responseClassType.equals(InvoiceV3Response.class)) {
      return DocumentConfig.<T>builder()
        .apiType(API_TYPE_OFF_THE_SHELF)
        .documentType("invoice")
        .builtInPostProcessing(UnaryOperator.identity())
        .converter((clazz, map) -> OBJECT_MAPPER.convertValue(map, clazz))
        .endpoint(Endpoint.builder()
          .apiKey(apiKey)
          .keyName("invoice")
          .owner(MINDEE)
          .version("3")
          .urlName("invoices")
          .build())
        .build();
    } else if (responseClassType.equals(InvoiceV4Response.class)) {
      return DocumentConfig.<T>builder()
        .apiType(API_TYPE_OFF_THE_SHELF)
        .documentType("invoice")
        .builtInPostProcessing(UnaryOperator.identity())
        .converter((clazz, map) -> OBJECT_MAPPER.convertValue(map, clazz))
        .endpoint(Endpoint.builder()
          .apiKey(apiKey)
          .keyName("invoice")
          .owner(MINDEE)
          .version("4")
          .urlName("invoices")
          .build())
        .build();
    } else if (responseClassType.equals(PassportV1Response.class)) {
      UnaryOperator<PassportV1Response> operator = PassportResponsePostProcessor::reconstructMrz;
      Function<PassportV1Response, PassportV1Response> finalOperator = operator.compose(
        PassportResponsePostProcessor::reconstructFullName);
      return DocumentConfig.<PassportV1Response>builder()
        .apiType(API_TYPE_OFF_THE_SHELF)
        .documentType("passport")
        .builtInPostProcessing(finalOperator)
        .converter((clazz, map) -> OBJECT_MAPPER.convertValue(map, clazz))
        .endpoint(Endpoint.builder()
          .apiKey(apiKey)
          .keyName("passport")
          .owner(MINDEE)
          .version("1")
          .urlName("passport")
          .build())
        .build();
    } else if (responseClassType.equals(FinancialDocumentResponse.class)) {
      return DocumentConfig.<T>builder()
        .apiType(API_TYPE_OFF_THE_SHELF)
        .documentType("financial_doc")
        .builtInPostProcessing(UnaryOperator.identity())
        .converter((clazz, map) -> OBJECT_MAPPER.convertValue(map, clazz))
        .endpoint(Endpoint.builder()
          .apiKey(apiKey)
          .keyName("invoice")
          .owner(MINDEE)
          .version("3")
          .urlName("invoices")
          .build())
        .endpoint(Endpoint.builder()
          .apiKey(apiKey)
          .keyName("receipt")
          .owner(MINDEE)
          .version("3")
          .urlName("expense_receipts")
          .build())
        .build();
    } else if (responseClassType.equals(ReceiptV3Response.class)) {

      return DocumentConfig.<ReceiptV3Response>builder()
        .apiType(API_TYPE_OFF_THE_SHELF)
        .documentType("receipt")
        .converter((clazz, map) -> OBJECT_MAPPER.convertValue(map, clazz))
        .builtInPostProcessing(UnaryOperator.identity())
        .endpoint(Endpoint.builder()
          .apiKey(apiKey)
          .keyName("receipt")
          .owner(MINDEE)
          .version("3")
          .urlName("expense_receipts")
          .build())
        .build();
    } else if (responseClassType.equals(ReceiptV4Response.class)) {

      return DocumentConfig.<ReceiptV4Response>builder()
        .apiType(API_TYPE_OFF_THE_SHELF)
        .documentType("receipt")
        .converter((clazz, map) -> OBJECT_MAPPER.convertValue(map, clazz))
        .builtInPostProcessing(UnaryOperator.identity())
        .endpoint(Endpoint.builder()
          .apiKey(apiKey)
          .keyName("receipt")
          .owner(MINDEE)
          .version("4")
          .urlName("expense_receipts")
          .build())
        .build();
    }
    return null;
  }

  static <T extends BaseDocumentResponse> ParseParameters parseParametersForResponseType(Class<T> responseType) {
    if (responseType.equals(PassportV1Response.class)) {
      return ParseParameters.builder()
        .documentType(PASSPORT)
        .accountName(MINDEE)
        .build();
    } else if (responseType.equals(FinancialDocumentResponse.class)) {
      return ParseParameters.builder()
        .documentType(FINANCIAL_DOCUMENT)
        .accountName(MINDEE)
        .build();
    } else if (responseType.equals(InvoiceV3Response.class) || responseType.equals(
      InvoiceV4Response.InvoiceV4Document.class)) {
      return ParseParameters.builder()
        .documentType(INVOICE)
        .accountName(MINDEE)
        .build();
    } else if (responseType.equals(ReceiptV3Response.class) || responseType.equals(
      ReceiptV4Response.class)) {
      return ParseParameters.builder()
        .documentType(RECEIPT)
        .accountName(MINDEE)
        .build();
    }
    return null;
  }


}
