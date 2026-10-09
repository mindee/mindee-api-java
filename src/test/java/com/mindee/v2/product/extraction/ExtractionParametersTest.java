package com.mindee.v2.product.extraction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.mindee.v2.product.extraction.params.ExtractionParameters;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Extraction Parameters")
public class ExtractionParametersTest {
  private static final String MODEL_ID = "test-model-id";

  @Test
  @DisplayName("should init with minimum values")
  void parameters_mustInit() {
    ExtractionParameters productParams = ExtractionParameters.builder(MODEL_ID).build();
    assertEquals(MODEL_ID, productParams.getModelId());
  }

  @Nested
  @DisplayName("Data Schema")
  class DataSchemaTests {
    private Map<String, Object> dataSchemaDict;
    private String dataSchemaString;

    @Test
    @DisplayName("should leave unset when not provided")
    void dataSchema_shouldLeaveUnsetWhenNotProvided() {
      ExtractionParameters inferenceParameters = ExtractionParameters.builder(MODEL_ID).build();
      assertNull(inferenceParameters.getDataSchema());
    }

    @Test
    @DisplayName("should initialize from a string")
    void dataSchemaString_shouldInitialize() {
      ExtractionParameters inferenceParameters = ExtractionParameters
        .builder(MODEL_ID)
        .dataSchema(dataSchemaString)
        .build();
      assertEquals(dataSchemaString, inferenceParameters.getDataSchema());
    }
  }
}
