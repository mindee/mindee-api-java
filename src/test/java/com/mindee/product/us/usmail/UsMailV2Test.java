package com.mindee.product.us.usmail;

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
 * Unit tests for UsMailV2.
 */
public class UsMailV2Test {

  protected PredictResponse<UsMailV2> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      UsMailV2.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/us_mail/response_v2/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<UsMailV2> response = getPrediction("empty");
    UsMailV2Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getSenderName().getValue());
    Assertions.assertNull(docPrediction.getSenderAddress().getCity());
    Assertions.assertNull(docPrediction.getSenderAddress().getComplete());
    Assertions.assertNull(docPrediction.getSenderAddress().getPostalCode());
    Assertions.assertNull(docPrediction.getSenderAddress().getState());
    Assertions.assertNull(docPrediction.getSenderAddress().getStreet());
    Assertions.assertTrue(docPrediction.getRecipientNames().isEmpty());
    Assertions.assertTrue(docPrediction.getRecipientAddresses().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<UsMailV2> response = getPrediction("complete");
    Document<UsMailV2> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/us_mail/response_v2/summary_full.rst"
    );
  }

}
