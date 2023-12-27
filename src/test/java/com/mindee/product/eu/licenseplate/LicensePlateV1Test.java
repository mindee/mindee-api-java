package com.mindee.product.eu.licenseplate;

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
 * Unit tests for LicensePlateV1.
 */
public class LicensePlateV1Test {

  protected PredictResponse<LicensePlateV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      LicensePlateV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/license_plates/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<LicensePlateV1> response = getPrediction("empty");
    LicensePlateV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertTrue(docPrediction.getLicensePlates().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<LicensePlateV1> response = getPrediction("complete");
    Document<LicensePlateV1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/license_plates/response_v1/summary_full.rst"
    );
  }

}
