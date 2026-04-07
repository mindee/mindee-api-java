package com.mindee.v2.parsing;

import static com.mindee.TestingUtilities.getResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindee.MindeeException;
import com.mindee.v2.product.extraction.ExtractionResponse;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalResponseTest {
  @Test
  void loadDocument_withPath_mustReturnValidLocalResponse() throws IOException {
    var localResponse = new LocalResponse(
      getResourcePath("v2/products/extraction/financial_document/complete.json")
    );
    ExtractionResponse loaded = localResponse.deserializeResponse(ExtractionResponse.class);

    assertNotNull(loaded, "Loaded InferenceResponse must not be null");
    assertEquals(
      "12345678-1234-1234-1234-123456789abc",
      loaded.getInference().getModel().getId(),
      "Model Id mismatch"
    );
    assertEquals(
      "John Smith",
      loaded
        .getInference()
        .getResult()
        .getFields()
        .get("supplier_name")
        .getSimpleField()
        .getValue(),
      "Supplier name mismatch"
    );
  }

  @Test
  void givenInvalidJsonInput_shouldThrow() {
    var localResponse = new LocalResponse("{invalid json");
    var err = Assertions
      .assertThrows(
        MindeeException.class,
        () -> localResponse.deserializeResponse(ExtractionResponse.class)
      );
    Assertions.assertEquals("Invalid JSON payload.", err.getMessage());
  }
}
