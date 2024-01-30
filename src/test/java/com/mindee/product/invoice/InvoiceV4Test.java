package com.mindee.product.invoice;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Unit tests for InvoiceV4.
 */
public class InvoiceV4Test {

  protected PredictResponse<InvoiceV4> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      InvoiceV4.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/invoices/response_v4/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<InvoiceV4> response = getPrediction("empty");
    InvoiceV4Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getLocaleField().getValue());
    Assertions.assertNull(docPrediction.getInvoiceNumber().getValue());
    Assertions.assertTrue(docPrediction.getReferenceNumbers().isEmpty());
    Assertions.assertNull(docPrediction.getInvoiceDateField().getValue());
    Assertions.assertNull(docPrediction.getDueDateField().getValue());
    Assertions.assertNull(docPrediction.getTotalNet().getValue());
    Assertions.assertNull(docPrediction.getTotalAmount().getValue());
    Assertions.assertTrue(docPrediction.getTaxes().isEmpty());
    Assertions.assertTrue(docPrediction.getSupplierPaymentDetails().isEmpty());
    Assertions.assertNull(docPrediction.getSupplierName().getValue());
    Assertions.assertTrue(docPrediction.getSupplierCompanyRegistrations().isEmpty());
    Assertions.assertNull(docPrediction.getSupplierAddress().getValue());
    Assertions.assertNull(docPrediction.getCustomerName().getValue());
    Assertions.assertTrue(docPrediction.getCustomerCompanyRegistrations().isEmpty());
    Assertions.assertNull(docPrediction.getCustomerAddress().getValue());
    Assertions.assertNotNull(docPrediction.getDocumentType().getValue());
    Assertions.assertTrue(docPrediction.getLineItems().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<InvoiceV4> response = getPrediction("complete");
    Document<InvoiceV4> doc = response.getDocument();
    String[] actualLines = doc.toString().split(System.lineSeparator());
    List<String> expectedLines = Files.readAllLines(
      Paths.get("src/test/resources/products/invoices/response_v4/summary_full.rst")
    );
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }
}
