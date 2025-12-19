package com.mindee.product.driverlicense;

import static com.mindee.TestingUtilities.assertStringEqualsFile;
import static com.mindee.TestingUtilities.getV1ResourcePathString;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.standard.ClassificationField;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for DriverLicenseV1.
 */
public class DriverLicenseV1Test {

  protected PredictResponse<DriverLicenseV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      DriverLicenseV1.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/driver_license/response_v1/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<DriverLicenseV1> response = getPrediction("empty");
    DriverLicenseV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getCountryCode().getValue());
    Assertions.assertNull(docPrediction.getState().getValue());
    Assertions.assertNull(docPrediction.getId().getValue());
    Assertions.assertNull(docPrediction.getCategory().getValue());
    Assertions.assertNull(docPrediction.getLastName().getValue());
    Assertions.assertNull(docPrediction.getFirstName().getValue());
    Assertions.assertNull(docPrediction.getDateOfBirth().getValue());
    Assertions.assertNull(docPrediction.getPlaceOfBirth().getValue());
    Assertions.assertNull(docPrediction.getExpiryDate().getValue());
    Assertions.assertNull(docPrediction.getIssuedDate().getValue());
    Assertions.assertNull(docPrediction.getIssuingAuthority().getValue());
    Assertions.assertNull(docPrediction.getMrz().getValue());
    Assertions.assertNull(docPrediction.getDdNumber().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<DriverLicenseV1> response = getPrediction("complete");
    Document<DriverLicenseV1> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/driver_license/response_v1/summary_full.rst")
    );
  }

}
