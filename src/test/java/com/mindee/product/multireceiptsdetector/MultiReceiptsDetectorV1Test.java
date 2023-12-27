package com.mindee.product.multireceiptsdetector;

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
 * Unit tests for MultiReceiptsDetectorV1.
 */
public class MultiReceiptsDetectorV1Test {

  protected PredictResponse<MultiReceiptsDetectorV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      MultiReceiptsDetectorV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/multi_receipts_detector/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<MultiReceiptsDetectorV1> response = getPrediction("empty");
    MultiReceiptsDetectorV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertTrue(docPrediction.getReceipts().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<MultiReceiptsDetectorV1> response = getPrediction("complete");
    Document<MultiReceiptsDetectorV1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/multi_receipts_detector/response_v1/summary_full.rst"
    );
  }

}
