package com.mindee.product.internationalid;

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
 * Unit tests for InternationalIdV1.
 */
public class InternationalIdV1Test {

  protected PredictResponse<InternationalIdV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      InternationalIdV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/international_id/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<InternationalIdV1> response = getPrediction("empty");
    InternationalIdV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertInstanceOf(ClassificationField.class, docPrediction.getDocumentType());
    Assertions.assertNull(docPrediction.getDocumentNumber().getValue());
    Assertions.assertNull(docPrediction.getCountryOfIssue().getValue());
    Assertions.assertTrue(docPrediction.getSurnames().isEmpty());
    Assertions.assertTrue(docPrediction.getGivenNames().isEmpty());
    Assertions.assertNull(docPrediction.getSex().getValue());
    Assertions.assertNull(docPrediction.getBirthDate().getValue());
    Assertions.assertNull(docPrediction.getBirthPlace().getValue());
    Assertions.assertNull(docPrediction.getNationality().getValue());
    Assertions.assertNull(docPrediction.getIssueDate().getValue());
    Assertions.assertNull(docPrediction.getExpiryDate().getValue());
    Assertions.assertNull(docPrediction.getAddress().getValue());
    Assertions.assertNull(docPrediction.getMrz1().getValue());
    Assertions.assertNull(docPrediction.getMrz2().getValue());
    Assertions.assertNull(docPrediction.getMrz3().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<InternationalIdV1> response = getPrediction("complete");
    Document<InternationalIdV1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/international_id/response_v1/summary_full.rst"
    );
  }

}
