package com.mindee.v1.product.receipt;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.standard.ClassificationField;
import static com.mindee.TestingUtilities.assertStringEqualsFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static com.mindee.TestingUtilities.getV1ResourcePathString;

/**
 * Unit tests for ReceiptV5.
 */
public class ReceiptV5Test {

  protected PredictResponse<ReceiptV5> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      ReceiptV5.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/expense_receipts/response_v5/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<ReceiptV5> response = getPrediction("empty");
    ReceiptV5Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getLocale().getValue());
    Assertions.assertInstanceOf(ClassificationField.class, docPrediction.getCategory());
    Assertions.assertInstanceOf(ClassificationField.class, docPrediction.getSubcategory());
    Assertions.assertInstanceOf(ClassificationField.class, docPrediction.getDocumentType());
    Assertions.assertNull(docPrediction.getDate().getValue());
    Assertions.assertNull(docPrediction.getTime().getValue());
    Assertions.assertNull(docPrediction.getTotalAmount().getValue());
    Assertions.assertNull(docPrediction.getTotalNet().getValue());
    Assertions.assertNull(docPrediction.getTotalTax().getValue());
    Assertions.assertNull(docPrediction.getTip().getValue());
    Assertions.assertTrue(docPrediction.getTaxes().isEmpty());
    Assertions.assertNull(docPrediction.getSupplierName().getValue());
    Assertions.assertTrue(docPrediction.getSupplierCompanyRegistrations().isEmpty());
    Assertions.assertNull(docPrediction.getSupplierAddress().getValue());
    Assertions.assertNull(docPrediction.getSupplierPhoneNumber().getValue());
    Assertions.assertNull(docPrediction.getReceiptNumber().getValue());
    Assertions.assertTrue(docPrediction.getLineItems().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<ReceiptV5> response = getPrediction("complete");
    Document<ReceiptV5> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/expense_receipts/response_v5/summary_full.rst")
    );
  }

}
