package com.mindee.product.receiptsitemsclassifier;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.product.ProductTestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

/**
 * Unit tests for ReceiptsItemsClassifierV1.
 */
public class ReceiptsItemsClassifierV1Test {

  protected PredictResponse<ReceiptsItemsClassifierV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      ReceiptsItemsClassifierV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/receipts_items_classifier/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<ReceiptsItemsClassifierV1> response = getPrediction("empty");
    ReceiptsItemsClassifierV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getMultipleReceipts().getValue());
    Assertions.assertNull(docPrediction.getIssueDate().getValue());
    Assertions.assertInstanceOf(ClassificationField.class, docPrediction.getExpenseCategory());
    Assertions.assertNull(docPrediction.getMerchantName().getValue());
    Assertions.assertNull(docPrediction.getMerchantAddress().getValue());
    Assertions.assertNull(docPrediction.getTotalAmount().getValue());
    Assertions.assertNull(docPrediction.getTip().getValue());
    Assertions.assertNull(docPrediction.getTotalTax().getValue());
    Assertions.assertNull(docPrediction.getCurrencyCode().getValue());
    Assertions.assertTrue(docPrediction.getLineItems().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<ReceiptsItemsClassifierV1> response = getPrediction("complete");
    Document<ReceiptsItemsClassifierV1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/receipts_items_classifier/response_v1/summary_full.rst"
    );
  }

}
