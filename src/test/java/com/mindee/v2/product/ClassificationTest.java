package com.mindee.v2.product;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.mindee.input.LocalResponse;
import com.mindee.parsing.v2.InferenceResponse;
import com.mindee.v2.product.classification.ClassificationResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Classification Model Tests")
public class ClassificationTest {
  private ClassificationResponse loadResponse(String filePath) throws IOException {
    LocalResponse localResponse = new LocalResponse(getV2ResourcePath(filePath));
    return localResponse.deserializeResponse(ClassificationResponse.class);
  }

  @Nested
  @DisplayName("Result with single value")
  class SinglePredictionTest {
    @Test
    @DisplayName("classification properties must be valid")
    void singleMustHaveValidProperties() throws IOException {
      ClassificationResponse response = loadResponse("products/classification/default_sample.json");
      assertNotNull(response.getInference());
      assertEquals(
        "invoice",
        response.getInference().getResult().getClassification().getDocumentType()
      );
      assertNull(response.getInference().getResult().getClassification().getExtractionResponse());
    }

    @Test
    @DisplayName("extraction properties must be valid")
    void singleExtractionMustHaveValidProperties() throws IOException {
      ClassificationResponse response = loadResponse(
        "products/classification/default_sample_extraction.json"
      );
      assertNotNull(response.getInference());
      assertEquals(
        "invoice",
        response.getInference().getResult().getClassification().getDocumentType()
      );
      InferenceResponse extractionResponse = response
        .getInference()
        .getResult()
        .getClassification()
        .getExtractionResponse();
      assertNotNull(extractionResponse);
      assertEquals(
        "Jiro Doi",
        extractionResponse
          .getInference()
          .getResult()
          .getFields()
          .getSimpleField("customer_name")
          .getStringValue()
      );
    }
  }
}
