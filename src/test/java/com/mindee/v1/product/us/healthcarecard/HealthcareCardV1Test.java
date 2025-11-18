package com.mindee.v1.product.us.healthcarecard;

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
 * Unit tests for HealthcareCardV1.
 */
public class HealthcareCardV1Test {

  protected PredictResponse<HealthcareCardV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      HealthcareCardV1.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/us_healthcare_cards/response_v1/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<HealthcareCardV1> response = getPrediction("empty");
    HealthcareCardV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getCompanyName().getValue());
    Assertions.assertNull(docPrediction.getPlanName().getValue());
    Assertions.assertNull(docPrediction.getMemberName().getValue());
    Assertions.assertNull(docPrediction.getMemberId().getValue());
    Assertions.assertNull(docPrediction.getIssuer80840().getValue());
    Assertions.assertTrue(docPrediction.getDependents().isEmpty());
    Assertions.assertNull(docPrediction.getGroupNumber().getValue());
    Assertions.assertNull(docPrediction.getPayerId().getValue());
    Assertions.assertNull(docPrediction.getRxBin().getValue());
    Assertions.assertNull(docPrediction.getRxId().getValue());
    Assertions.assertNull(docPrediction.getRxGrp().getValue());
    Assertions.assertNull(docPrediction.getRxPcn().getValue());
    Assertions.assertTrue(docPrediction.getCopays().isEmpty());
    Assertions.assertNull(docPrediction.getEnrollmentDate().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<HealthcareCardV1> response = getPrediction("complete");
    Document<HealthcareCardV1> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/us_healthcare_cards/response_v1/summary_full.rst")
    );
  }

}
