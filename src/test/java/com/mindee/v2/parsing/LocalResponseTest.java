package com.mindee.v2.parsing;

import static com.mindee.TestingUtilities.getResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindee.MindeeException;
import com.mindee.v2.product.extraction.ExtractionResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 – Load Local Response")
public class LocalResponseTest {
  private static final String SIGNATURE = "79dd6572f8a97822fb12f2f72bc84ecdc7c968dede712cf23a256ac3eac593d4";
  private static final String DUMMY_SECRET_KEY = "ogNjY44MhvKPGTtVsI8zG82JqWQa68woYQH";

  private static void assertLocalResponse(LocalResponse localResponse) {
    assertFalse(localResponse.isValidHmacSignature(DUMMY_SECRET_KEY, "invalid signature"));
    assertFalse(localResponse.isValidHmacSignature(DUMMY_SECRET_KEY, null));
    assertFalse(localResponse.isValidHmacSignature(null, SIGNATURE));
    assertFalse(localResponse.isValidHmacSignature(null, null));
    assertEquals(SIGNATURE, localResponse.getHmacSignature(DUMMY_SECRET_KEY));
    assertTrue(localResponse.isValidHmacSignature(DUMMY_SECRET_KEY, SIGNATURE));

    ExtractionResponse response = localResponse.deserializeResponse(ExtractionResponse.class);
    assertNotNull(response, "Loaded ExtractionResponse must not be null");
    assertEquals(
      "12345678-1234-1234-1234-123456789abc",
      response.getInference().getModel().getId(),
      "Model Id mismatch"
    );
    assertEquals(
      "John Smith",
      response
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
  void loadDocument_withPath_mustReturnValidLocalResponse() throws IOException {
    var localResponse = new LocalResponse(
      getResourcePath("v2/products/extraction/financial_document/complete.json")
    );
    assertLocalResponse(localResponse);
  }

  @Test
  void givenInvalidJsonInput_shouldThrow() {
    var localResponse = new LocalResponse("{invalid json");
    var err = assertThrows(
      MindeeException.class,
      () -> localResponse.deserializeResponse(ExtractionResponse.class)
    );
    assertEquals("Invalid JSON payload.", err.getMessage());
  }
}
