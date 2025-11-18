package com.mindee.v1.product.fr.healthcard;

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
 * Unit tests for HealthCardV1.
 */
public class HealthCardV1Test {

  protected PredictResponse<HealthCardV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      HealthCardV1.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/french_healthcard/response_v1/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<HealthCardV1> response = getPrediction("empty");
    HealthCardV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertTrue(docPrediction.getGivenNames().isEmpty());
    Assertions.assertNull(docPrediction.getSurname().getValue());
    Assertions.assertNull(docPrediction.getSocialSecurity().getValue());
    Assertions.assertNull(docPrediction.getIssuanceDate().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<HealthCardV1> response = getPrediction("complete");
    Document<HealthCardV1> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/french_healthcard/response_v1/summary_full.rst")
    );
  }

}
