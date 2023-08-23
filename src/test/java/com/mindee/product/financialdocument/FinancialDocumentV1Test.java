package com.mindee.product.financialdocument;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.PredictResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class FinancialDocumentV1Test {

  @Test
  void givenFinancialV1_withInvoice_whenDeserialized_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(PredictResponse.class,
      FinancialDocumentV1.class);
    PredictResponse<FinancialDocumentV1> prediction = objectMapper.readValue(
        new File("src/test/resources/products/financial_document/response_v1/complete_invoice.json"),
        type);

    String[] actualLines = prediction.getDocument().toString().split(System.lineSeparator());
    List<String> expectedLines = Files
        .readAllLines(Paths.get("src/test/resources/products/financial_document/response_v1/summary_full_invoice.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void givenFinancialV1_withInvoice_firstPage_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(PredictResponse.class,
      FinancialDocumentV1.class);
    PredictResponse<FinancialDocumentV1> prediction = objectMapper.readValue(
      new File("src/test/resources/products/financial_document/response_v1/complete_invoice.json"),
      type);

    String[] actualLines = prediction.getDocument().getInference()
      .getPages()
      .get(0).toString().split(System.lineSeparator());
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/products/financial_document/response_v1/summary_page0_invoice.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void givenFinancialV1_withReceipt_whenDeserialized_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(PredictResponse.class,
      FinancialDocumentV1.class);
    PredictResponse<FinancialDocumentV1> prediction = objectMapper.readValue(
      new File("src/test/resources/products/financial_document/response_v1/complete_receipt.json"),
      type);

    String[] actualLines = prediction.getDocument().toString().split(System.lineSeparator());
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/products/financial_document/response_v1/summary_full_receipt.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void givenFinancialV1_withReceipt_firstPage_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(PredictResponse.class,
      FinancialDocumentV1.class);
    PredictResponse<FinancialDocumentV1> prediction = objectMapper.readValue(
      new File("src/test/resources/products/financial_document/response_v1/complete_receipt.json"),
      type);

    String[] actualLines = prediction.getDocument().getInference()
      .getPages()
      .get(0).toString().split(System.lineSeparator());
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/products/financial_document/response_v1/summary_page0_receipt.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

}
