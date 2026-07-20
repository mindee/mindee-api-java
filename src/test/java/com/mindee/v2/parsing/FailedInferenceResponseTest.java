package com.mindee.v2.parsing;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Failed Inference Tests")
public class FailedInferenceResponseTest {
  @Test
  @DisplayName("properties must be valid")
  void whenFailed_mustLoad() throws IOException {
    var localResponse = new LocalResponse(
      getV2ResourcePath("errors/webhook_error_500_failed.json")
    );
    var response = localResponse.deserializeResponse(FailedInferenceResponse.class);
    assertNotNull(response);
    assertEquals("12345678-1234-1234-1234-123456789ABC", response.getInferenceId());
    assertEquals("default_sample.jpg", response.getFileName());
    assertEquals("dummy-alias.jpg", response.getFileAlias());
    assertEquals("dummy-alias.jpg", response.getFileAlias());
    assertInstanceOf(LocalDateTime.class, response.getCreatedAt());
    assertNotNull(response.getError());
    assertNotNull(response.getError());
    assertEquals(500, response.getError().getStatus());
    assertEquals("500-012", response.getError().getCode());
  }
}
