package com.mindee.v2.product;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindee.v2.parsing.LocalResponse;
import com.mindee.v2.product.classification.ClassificationResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Classification Model Tests")
public class ClassificationTest {
  private ClassificationResponse loadResponse(String filePath) throws IOException {
    var localResponse = new LocalResponse(getV2ResourcePath(filePath));
    return localResponse.deserializeResponse(ClassificationResponse.class);
  }

  @Nested
  @DisplayName("Result with single value")
  class SinglePredictionTest {
    @Test
    @DisplayName("all properties must be valid")
    void mustHaveValidProperties() throws IOException {
      ClassificationResponse response = loadResponse(
        "products/classification/classification_single.json"
      );
      assertNotNull(response.getInference());
      assertEquals(
        "invoice",
        response.getInference().getResult().getClassification().getDocumentType()
      );
    }
  }
}
