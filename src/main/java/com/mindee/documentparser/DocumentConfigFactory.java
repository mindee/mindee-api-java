package com.mindee.documentparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mindee.http.Endpoint;
import com.mindee.model.customdocument.CustomDocumentDeserializer;
import com.mindee.model.customdocument.CustomDocumentResponse;
import com.mindee.model.documenttype.BaseDocumentResponse;
import com.mindee.model.documenttype.PassportResponse;
import com.mindee.model.documenttype.ReceiptResponse;
import com.mindee.model.postprocessing.PassportResponsePostProcessor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.Value;

final class DocumentConfigFactory {

  private static final String API_TYPE_CUSTOM = "api_builder";
  private static final String API_TYPE_OFF_THE_SHELF = "off_the_shelf";
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String RECEIPT = "receipt";
  private static final String INVOICE = "invoice";
  private static final String FINANCIAL_DOCUMENT = "financial_doc";
  private static final String PASSPORT = "passport";
  private static final String ENV_API_KEY = "MINDEE_API_KEY";
  private static final Map<ObjectMapperMapKey, ObjectMapper> customMappers = new HashMap<>();

  private DocumentConfigFactory() {
  }

  static String getApiKeyFromEnvironmentVariable() {
    return System.getenv(ENV_API_KEY);
  }

  static <T extends BaseDocumentResponse> DocumentConfig<T> getDocumentConfigFromApiKey(
      String apiKey, String docType, String accountName) {
    if (apiKey == null || apiKey.trim().length() == 0) {
      throw new IllegalArgumentException("API KEY is null or blank");
    }

    if (docType == null) {
      throw new IllegalArgumentException("doc type cannot be blank");
    }

    if (docType.equalsIgnoreCase(INVOICE) || docType.equalsIgnoreCase(RECEIPT)
        || docType.equalsIgnoreCase(PASSPORT)) {
      return getDocumentConfigForOffTheShelfDocType(docType, accountName, apiKey);
    } else if (docType.equalsIgnoreCase(FINANCIAL_DOCUMENT)) {
      return getDocumentConfigForOffTheShelfDocType(docType, accountName, apiKey);
    } else {
      return getDocumentConfigForCustomDocType(docType, accountName, apiKey, "1", "document",
          "pages");
    }
  }

  static <T extends BaseDocumentResponse> DocumentConfig<T> getDocumentConfigForCustomDocType(
      String docType,
      String owner, String apiKey, String version, String singularName, String pluralName) {
    if (docType == null || owner == null || apiKey == null || version == null
        || singularName == null || pluralName == null) {
      throw new IllegalArgumentException(
          "DocType, Owner, ApiKey, Version, Singular Name, and Plural Name cannot be null");
    }
    if (singularName.equals(pluralName)) {
      throw new IllegalArgumentException("Singular name and plural name cannot be the same");
    }
    ObjectMapperMapKey objectMapperMapKey = new ObjectMapperMapKey(singularName, pluralName);
    final ObjectMapper objectMapper;
    if (customMappers.containsKey(objectMapperMapKey)) {
      objectMapper = customMappers.get(objectMapperMapKey);
    } else {
      objectMapper = new ObjectMapper();
      SimpleModule module = new SimpleModule();
      module.addDeserializer(
          CustomDocumentResponse.class, new CustomDocumentDeserializer());
      objectMapper.registerModule(module);
      customMappers.put(objectMapperMapKey, objectMapper);
    }
    return DocumentConfig.<T>builder()
        .apiType(API_TYPE_CUSTOM)
        .documentType(docType)
        .singularName(singularName)
        .pluralName(pluralName)
        .converter((clazz, map) -> objectMapper.convertValue(map, clazz))
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

  static <T extends BaseDocumentResponse> DocumentConfig getDocumentConfigForOffTheShelfDocType(
      String docType,
      String owner, String apiKey) {
    if (docType == null || owner == null || apiKey == null) {
      throw new IllegalArgumentException("DocType, Owner, and ApiKey cannot be null");
    }

    if (docType.equals("receipt")) {

      return DocumentConfig.<ReceiptResponse>builder()
          .apiType(API_TYPE_OFF_THE_SHELF)
          .documentType("receipt")
          .singularName("receipt")
          .pluralName("receipts")
          .converter((clazz, map) -> OBJECT_MAPPER.convertValue(map, clazz))
          .builtInPostProcessing(UnaryOperator.identity())
          .endpoint(Endpoint.builder()
              .apiKey(apiKey)
              .keyName("receipt")
              .owner(owner)
              .version("3")
              .urlName("expense_receipts")
              .build())
          .build();
    } else if (docType.equals("invoice")) {
      return DocumentConfig.<T>builder()
          .apiType(API_TYPE_OFF_THE_SHELF)
          .documentType("invoice")
          .singularName("invoice")
          .pluralName("invoices")
          .builtInPostProcessing(UnaryOperator.identity())
          .converter((clazz, map) -> OBJECT_MAPPER.convertValue(map, clazz))
          .endpoint(Endpoint.builder()
              .apiKey(apiKey)
              .keyName("invoice")
              .owner(owner)
              .version("3")
              .urlName("invoices")
              .build())
          .build();
    } else if (docType.equals("passport")) {
      UnaryOperator<PassportResponse> operator = PassportResponsePostProcessor::reconstructMrz;
      Function<PassportResponse, PassportResponse> finalOperator = operator.compose(
          PassportResponsePostProcessor::reconstructFullName);
      return DocumentConfig.<PassportResponse>builder()
          .apiType(API_TYPE_OFF_THE_SHELF)
          .documentType("passport")
          .singularName("passport")
          .pluralName("passports")
          .builtInPostProcessing(finalOperator)
          .converter((clazz, map) -> OBJECT_MAPPER.convertValue(map, clazz))
          .endpoint(Endpoint.builder()
              .apiKey(apiKey)
              .keyName("passport")
              .owner(owner)
              .version("1")
              .urlName("passport")
              .build())
          .build();
    } else if (docType.equals("financial_doc")) {
      return DocumentConfig.<T>builder()
          .apiType(API_TYPE_OFF_THE_SHELF)
          .documentType("financial_doc")
          .singularName("financialDoc")
          .pluralName("financialDocs")
          .builtInPostProcessing(UnaryOperator.identity())
          .converter((clazz, map) -> OBJECT_MAPPER.convertValue(map, clazz))
          .endpoint(Endpoint.builder()
              .apiKey(apiKey)
              .keyName("invoice")
              .owner(owner)
              .version("3")
              .urlName("invoices")
              .build())
          .endpoint(Endpoint.builder()
              .apiKey(apiKey)
              .keyName("receipt")
              .owner(owner)
              .version("3")
              .urlName("expense_receipts")
              .build())
          .build();
    }
    return null;
  }

  @Value
  private static class ObjectMapperMapKey {

    private final String singular;
    private final String plural;
  }

}
