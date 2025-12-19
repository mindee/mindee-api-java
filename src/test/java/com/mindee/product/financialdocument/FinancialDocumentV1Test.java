package com.mindee.product.financialdocument;

import static com.mindee.TestingUtilities.getV1ResourcePath;
import static com.mindee.TestingUtilities.getV1ResourcePathString;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FinancialDocumentV1Test {

  @Test
  void givenFinancialV1_withInvoice_whenDeserialized_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, FinancialDocumentV1.class);
    PredictResponse<FinancialDocumentV1> prediction = objectMapper
      .readValue(
        new File(
          getV1ResourcePathString("products/financial_document/response_v1/complete_invoice.json")
        ),
        type
      );

    String[] actualLines = prediction.getDocument().toString().split(System.lineSeparator());
    List<String> expectedLines = Files
      .readAllLines(
        getV1ResourcePath("products/financial_document/response_v1/summary_full_invoice.rst")
      );
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void givenFinancialV1_withInvoice_firstPage_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, FinancialDocumentV1.class);
    PredictResponse<FinancialDocumentV1> prediction = objectMapper
      .readValue(
        new File(
          getV1ResourcePathString("products/financial_document/response_v1/complete_invoice.json")
        ),
        type
      );

    String[] actualLines = prediction
      .getDocument()
      .getInference()
      .getPages()
      .get(0)
      .toString()
      .split(System.lineSeparator());
    List<String> expectedLines = Files
      .readAllLines(
        getV1ResourcePath("products/financial_document/response_v1/summary_page0_invoice.rst")
      );
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void givenFinancialV1_withReceipt_whenDeserialized_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, FinancialDocumentV1.class);
    PredictResponse<FinancialDocumentV1> prediction = objectMapper
      .readValue(
        new File(
          getV1ResourcePathString("products/financial_document/response_v1/complete_receipt.json")
        ),
        type
      );

    String[] actualLines = prediction.getDocument().toString().split(System.lineSeparator());
    List<String> expectedLines = Files
      .readAllLines(
        getV1ResourcePath("products/financial_document/response_v1/summary_full_receipt.rst")
      );
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void givenFinancialV1_withReceipt_firstPage_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, FinancialDocumentV1.class);
    PredictResponse<FinancialDocumentV1> prediction = objectMapper
      .readValue(
        new File(
          getV1ResourcePathString("products/financial_document/response_v1/complete_receipt.json")
        ),
        type
      );

    String[] actualLines = prediction
      .getDocument()
      .getInference()
      .getPages()
      .get(0)
      .toString()
      .split(System.lineSeparator());
    List<String> expectedLines = Files
      .readAllLines(
        getV1ResourcePath("products/financial_document/response_v1/summary_page0_receipt.rst")
      );
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void givenFinancialV1_withEmptyDocument_whenDeserialized_MustHaveExpectedValues() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, FinancialDocumentV1.class);
    PredictResponse<FinancialDocumentV1> response = objectMapper
      .readValue(
        new File(getV1ResourcePathString("products/financial_document/response_v1/empty.json")),
        type
      );

    Document<FinancialDocumentV1> doc = response.getDocument();
    FinancialDocumentV1Document prediction = doc.getInference().getPrediction();

    Assertions.assertEquals("EUR;", prediction.getLocale().toString());
    Assertions.assertNull(prediction.getTotalAmount().getValue());
    Assertions.assertNull(prediction.getTotalNet().getValue());
    Assertions.assertNull(prediction.getTotalTax().getValue());
    Assertions.assertNull(prediction.getDate().getValue());
    Assertions.assertNull(prediction.getInvoiceNumber().getValue());
    Assertions.assertNull(prediction.getBillingAddress().getValue());
    Assertions.assertNull(prediction.getDueDate().getValue());
    Assertions.assertNull(prediction.getDocumentNumber().getValue());
    Assertions.assertEquals("EXPENSE RECEIPT", prediction.getDocumentType().getValue());
    Assertions.assertEquals("EXPENSE RECEIPT", prediction.getDocumentTypeExtended().getValue());
    Assertions.assertNull(prediction.getSupplierName().getValue());
    Assertions.assertNull(prediction.getSupplierAddress().getValue());
    Assertions.assertNull(prediction.getCustomerId().getValue());
    Assertions.assertNull(prediction.getCustomerName().getValue());
    Assertions.assertNull(prediction.getCustomerAddress().getValue());
    Assertions.assertTrue(prediction.getCustomerCompanyRegistrations().isEmpty());
    Assertions.assertTrue(prediction.getTaxes().isEmpty());
    Assertions.assertTrue(prediction.getSupplierPaymentDetails().isEmpty());
    Assertions.assertTrue(prediction.getSupplierCompanyRegistrations().isEmpty());
    Assertions.assertNull(prediction.getTip().getValue());
    Assertions.assertNull(prediction.getTotalAmount().getValue());
    Assertions.assertNull(prediction.getTotalNet().getValue());
    Assertions.assertNull(prediction.getTotalTax().getValue());
    Assertions.assertNull(prediction.getDate().getValue());
    Assertions.assertNull(prediction.getTime().getValue());
    Assertions.assertNull(prediction.getSupplierName().getValue());
    Assertions.assertNull(prediction.getSupplierAddress().getValue());
    Assertions.assertTrue(prediction.getTaxes().isEmpty());
  }
}
