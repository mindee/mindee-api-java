package com.mindee.product.us.bankcheck;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Page;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.product.ProductTestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

/**
 * Unit tests for BankCheckV1.
 */
public class BankCheckV1Test {

  protected PredictResponse<BankCheckV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      BankCheckV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/bank_check/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<BankCheckV1> response = getPrediction("empty");
    BankCheckV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getDate().getValue());
    Assertions.assertNull(docPrediction.getAmount().getValue());
    Assertions.assertTrue(docPrediction.getPayees().isEmpty());
    Assertions.assertNull(docPrediction.getRoutingNumber().getValue());
    Assertions.assertNull(docPrediction.getAccountNumber().getValue());
    Assertions.assertNull(docPrediction.getCheckNumber().getValue());
    BankCheckV1Page pagePrediction = response.getDocument().getInference().getPages().get(0).getPrediction();
    Assertions.assertEquals(pagePrediction.getCheckPosition().toString(), "");
    Assertions.assertTrue(pagePrediction.getSignaturesPositions().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<BankCheckV1> response = getPrediction("complete");
    Document<BankCheckV1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/bank_check/response_v1/summary_full.rst"
    );
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidPage0Summary() throws IOException {
    PredictResponse<BankCheckV1> response = getPrediction("complete");
    Page<BankCheckV1Page> page = response.getDocument().getInference().getPages().get(0);
    ProductTestHelper.assertStringEqualsFile(
        page.toString(),
        "src/test/resources/products/bank_check/response_v1/summary_page0.rst"
    );
  }
}
