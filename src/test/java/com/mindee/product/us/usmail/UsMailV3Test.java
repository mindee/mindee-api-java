package com.mindee.product.us.usmail;

import static com.mindee.TestingUtilities.assertStringEqualsFile;
import static com.mindee.TestingUtilities.getV1ResourcePathString;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for UsMailV3.
 */
public class UsMailV3Test {

  protected PredictResponse<UsMailV3> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      UsMailV3.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/us_mail/response_v3/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<UsMailV3> response = getPrediction("empty");
    UsMailV3Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getSenderName().getValue());
    Assertions.assertNull(docPrediction.getSenderAddress().getCity());
    Assertions.assertNull(docPrediction.getSenderAddress().getComplete());
    Assertions.assertNull(docPrediction.getSenderAddress().getPostalCode());
    Assertions.assertNull(docPrediction.getSenderAddress().getState());
    Assertions.assertNull(docPrediction.getSenderAddress().getStreet());
    Assertions.assertTrue(docPrediction.getRecipientNames().isEmpty());
    Assertions.assertTrue(docPrediction.getRecipientAddresses().isEmpty());
    Assertions.assertNull(docPrediction.getIsReturnToSender().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<UsMailV3> response = getPrediction("complete");
    Document<UsMailV3> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/us_mail/response_v3/summary_full.rst")
    );
  }

}
