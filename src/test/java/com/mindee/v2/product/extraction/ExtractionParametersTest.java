package com.mindee.v2.product.extraction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.mindee.v2.product.extraction.params.ExtractionParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Extraction Parameters")
public class ExtractionParametersTest {
  private static final String MODEL_ID = "test-model-id";
  private static final String DATA_SCHEMA = "{\"fields\":{}}";

  @Test
  @DisplayName("should init with only required parameters")
  void parameters_mustInitWithRequired() {
    var productParams = ExtractionParameters.builder(MODEL_ID).build();

    var requestParams = productParams.getRequestParameters();
    assertEquals(MODEL_ID, requestParams.get("model_id"));
    assertFalse(requestParams.containsKey("alias"));
    assertFalse(requestParams.containsKey("webhook_ids"));
    assertFalse(requestParams.containsKey("rag"));
    assertFalse(requestParams.containsKey("raw_text"));
    assertFalse(requestParams.containsKey("polygon"));
    assertFalse(requestParams.containsKey("confidence"));
    assertFalse(requestParams.containsKey("text_context"));
    assertFalse(requestParams.containsKey("data_schema"));
  }

  @Test
  @DisplayName("should init with all parameters")
  void parameters_mustInitWithAll() {
    var productParams = ExtractionParameters
      .builder(MODEL_ID)
      .alias("test-alias")
      .webhookIds(new String[] { "webhook-1", "webhook-2" })
      .rag(true)
      .rawText(false)
      .polygon(true)
      .confidence(false)
      .textContext("test context")
      .dataSchema(DATA_SCHEMA)
      .build();

    var requestParams = productParams.getRequestParameters();
    assertEquals(MODEL_ID, requestParams.get("model_id"));
    assertEquals("test-alias", requestParams.get("alias"));
    assertEquals("webhook-1,webhook-2", requestParams.get("webhook_ids"));
    assertEquals("true", requestParams.get("rag"));
    assertEquals("false", requestParams.get("raw_text"));
    assertEquals("true", requestParams.get("polygon"));
    assertEquals("false", requestParams.get("confidence"));
    assertEquals("test context", requestParams.get("text_context"));
    assertEquals(DATA_SCHEMA, requestParams.get("data_schema"));
  }

  @Nested
  @DisplayName("Data Schema")
  class DataSchemaTests {
    @Test
    @DisplayName("should leave unset when not provided")
    void dataSchema_shouldLeaveUnsetWhenNotProvided() {
      var inferenceParameters = ExtractionParameters.builder(MODEL_ID).build();
      var requestParams = inferenceParameters.getRequestParameters();
      assertFalse(requestParams.containsKey("data_schema"));
    }

    @Test
    @DisplayName("should initialize from a string")
    void dataSchemaString_shouldInitialize() {
      var inferenceParameters = ExtractionParameters
        .builder(MODEL_ID)
        .dataSchema(DATA_SCHEMA)
        .build();
      var requestParams = inferenceParameters.getRequestParameters();
      assertEquals(DATA_SCHEMA, requestParams.get("data_schema"));
    }
  }
}
