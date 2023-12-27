package com.mindee.product.cropper;

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
 * Unit tests for CropperV1.
 */
public class CropperV1Test {

  protected PredictResponse<CropperV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      CropperV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/cropper/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<CropperV1> response = getPrediction("empty");
    CropperV1Page pagePrediction = response.getDocument().getInference().getPages().get(0).getPrediction();
    Assertions.assertTrue(pagePrediction.getCropping().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<CropperV1> response = getPrediction("complete");
    Document<CropperV1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/cropper/response_v1/summary_full.rst"
    );
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidPage0Summary() throws IOException {
    PredictResponse<CropperV1> response = getPrediction("complete");
    Page<CropperV1Page> page = response.getDocument().getInference().getPages().get(0);
    ProductTestHelper.assertStringEqualsFile(
        page.toString(),
        "src/test/resources/products/cropper/response_v1/summary_page0.rst"
    );
  }
}
