package com.mindee.product.us.driverlicense;

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
      new File("src/test/resources/products/us_driver_license/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<DriverLicenseV1> response = getPrediction("empty");
    DriverLicenseV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getState().getValue());
    Assertions.assertNull(docPrediction.getDriverLicenseId().getValue());
    Assertions.assertNull(docPrediction.getExpiryDate().getValue());
    Assertions.assertNull(docPrediction.getIssuedDate().getValue());
    Assertions.assertNull(docPrediction.getLastName().getValue());
    Assertions.assertNull(docPrediction.getFirstName().getValue());
    Assertions.assertNull(docPrediction.getAddress().getValue());
    Assertions.assertNull(docPrediction.getDateOfBirth().getValue());
    Assertions.assertNull(docPrediction.getRestrictions().getValue());
    Assertions.assertNull(docPrediction.getEndorsements().getValue());
    Assertions.assertNull(docPrediction.getDlClass().getValue());
    Assertions.assertNull(docPrediction.getSex().getValue());
    Assertions.assertNull(docPrediction.getHeight().getValue());
    Assertions.assertNull(docPrediction.getWeight().getValue());
    Assertions.assertNull(docPrediction.getHairColor().getValue());
    Assertions.assertNull(docPrediction.getEyeColor().getValue());
    Assertions.assertNull(docPrediction.getDdNumber().getValue());
    DriverLicenseV1Page pagePrediction = response.getDocument().getInference().getPages().get(0).getPrediction();
    Assertions.assertEquals(pagePrediction.getPhoto().toString(), "");
    Assertions.assertEquals(pagePrediction.getSignature().toString(), "");
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<DriverLicenseV1> response = getPrediction("complete");
    Document<DriverLicenseV1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/us_driver_license/response_v1/summary_full.rst"
    );
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidPage0Summary() throws IOException {
    PredictResponse<DriverLicenseV1> response = getPrediction("complete");
    Page<DriverLicenseV1Page> page = response.getDocument().getInference().getPages().get(0);
    ProductTestHelper.assertStringEqualsFile(
        page.toString(),
        "src/test/resources/products/us_driver_license/response_v1/summary_page0.rst"
    );
  }
}
