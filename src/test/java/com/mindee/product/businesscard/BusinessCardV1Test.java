package com.mindee.product.businesscard;

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
 * Unit tests for BusinessCardV1.
 */
public class BusinessCardV1Test {

  protected PredictResponse<BusinessCardV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      BusinessCardV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/business_card/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<BusinessCardV1> response = getPrediction("empty");
    BusinessCardV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getFirstname().getValue());
    Assertions.assertNull(docPrediction.getLastname().getValue());
    Assertions.assertNull(docPrediction.getJobTitle().getValue());
    Assertions.assertNull(docPrediction.getCompany().getValue());
    Assertions.assertNull(docPrediction.getEmail().getValue());
    Assertions.assertNull(docPrediction.getPhoneNumber().getValue());
    Assertions.assertNull(docPrediction.getMobileNumber().getValue());
    Assertions.assertNull(docPrediction.getFaxNumber().getValue());
    Assertions.assertNull(docPrediction.getAddress().getValue());
    Assertions.assertNull(docPrediction.getWebsite().getValue());
    Assertions.assertTrue(docPrediction.getSocialMedia().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<BusinessCardV1> response = getPrediction("complete");
    Document<BusinessCardV1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/business_card/response_v1/summary_full.rst"
    );
  }

}
