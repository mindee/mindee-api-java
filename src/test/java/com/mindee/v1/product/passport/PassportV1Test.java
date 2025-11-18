package com.mindee.v1.product.passport;

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
 * Unit tests for PassportV1.
 */
public class PassportV1Test {

  protected PredictResponse<PassportV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      PassportV1.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/passport/response_v1/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<PassportV1> response = getPrediction("empty");
    PassportV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getCountry().getValue());
    Assertions.assertNull(docPrediction.getIdNumber().getValue());
    Assertions.assertTrue(docPrediction.getGivenNames().isEmpty());
    Assertions.assertNull(docPrediction.getSurname().getValue());
    Assertions.assertNull(docPrediction.getBirthDate().getValue());
    Assertions.assertNull(docPrediction.getBirthPlace().getValue());
    Assertions.assertNull(docPrediction.getGender().getValue());
    Assertions.assertNull(docPrediction.getIssuanceDate().getValue());
    Assertions.assertNull(docPrediction.getExpiryDate().getValue());
    Assertions.assertNull(docPrediction.getMrz1().getValue());
    Assertions.assertNull(docPrediction.getMrz2().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<PassportV1> response = getPrediction("complete");
    Document<PassportV1> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/passport/response_v1/summary_full.rst")
    );
  }

}
