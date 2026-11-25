package com.mindee.v2.product.split;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.mindee.v2.product.split.params.SplitParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Split Parameters")
public class SplitParametersTest {
  private static final String MODEL_ID = "test-model-id";

  @Test
  @DisplayName("should init with only required parameters")
  void parameters_mustInitWithRequired() {
    var productParams = SplitParameters.builder(MODEL_ID).build();

    var requestParams = productParams.getRequestParameters();
    assertEquals(MODEL_ID, requestParams.get("model_id"));
    assertFalse(requestParams.containsKey("alias"));
    assertFalse(requestParams.containsKey("webhook_ids"));
    assertFalse(requestParams.containsKey("extraction_text_context"));
  }

  @Test
  @DisplayName("should init with all parameters")
  void parameters_mustInitWithAll() {
    var productParams = SplitParameters
      .builder(MODEL_ID)
      .alias("test-alias")
      .webhookIds(new String[] { "webhook-1", "webhook-2" })
      .extractionTextContext("context")
      .build();

    var requestParams = productParams.getRequestParameters();
    assertEquals(MODEL_ID, requestParams.get("model_id"));
    assertEquals("test-alias", requestParams.get("alias"));
    assertEquals("webhook-1,webhook-2", requestParams.get("webhook_ids"));
    assertEquals("context", requestParams.get("extraction_text_context"));
  }
}
