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
 * Unit tests for InternationalIdV2.
 */
public class InternationalIdV2Test {

  protected PredictResponse<InternationalIdV2> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      InternationalIdV2.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/international_id/response_v2/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<InternationalIdV2> response = getPrediction("empty");
    InternationalIdV2Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertInstanceOf(ClassificationField.class, docPrediction.getDocumentType());
    Assertions.assertNull(docPrediction.getDocumentNumber().getValue());
    Assertions.assertTrue(docPrediction.getSurnames().isEmpty());
    Assertions.assertTrue(docPrediction.getGivenNames().isEmpty());
    Assertions.assertNull(docPrediction.getSex().getValue());
    Assertions.assertNull(docPrediction.getBirthDate().getValue());
    Assertions.assertNull(docPrediction.getBirthPlace().getValue());
    Assertions.assertNull(docPrediction.getNationality().getValue());
    Assertions.assertNull(docPrediction.getPersonalNumber().getValue());
    Assertions.assertNull(docPrediction.getCountryOfIssue().getValue());
    Assertions.assertNull(docPrediction.getStateOfIssue().getValue());
    Assertions.assertNull(docPrediction.getIssueDate().getValue());
    Assertions.assertNull(docPrediction.getExpiryDate().getValue());
    Assertions.assertNull(docPrediction.getAddress().getValue());
    Assertions.assertNull(docPrediction.getMrzLine1().getValue());
    Assertions.assertNull(docPrediction.getMrzLine2().getValue());
    Assertions.assertNull(docPrediction.getMrzLine3().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<InternationalIdV2> response = getPrediction("complete");
    Document<InternationalIdV2> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/international_id/response_v2/summary_full.rst"
    );
  }

}
