package com.mindee.v2.product;

import static com.mindee.TestingUtilities.getResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.clientOptions.PollingOptions;
import com.mindee.v2.product.split.SplitResponse;
import com.mindee.v2.product.split.params.SplitParameters;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@DisplayName("MindeeV2 –Integration Tests - Split")
class SplitIT {

  private MindeeClient mindeeClient;
  private String modelId;

  @BeforeAll
  void setUp() {
    var apiKey = System.getenv("MINDEE_V2_API_KEY");
    modelId = System.getenv("MINDEE_V2_SE_TESTS_SPLIT_MODEL_ID");
    mindeeClient = new MindeeClient(apiKey);
  }

  @Test
  @DisplayName("Empty, multi-page PDF – enqueue & parse must succeed")
  void parseFile_emptyMultiPage_mustSucceed() throws IOException, InterruptedException {
    var source = new LocalInputSource(getResourcePath("file_types/pdf/multipage_cut-2.pdf"));
    var params = SplitParameters
      .builder(modelId)
      .alias("java_integration-test_split_multipage")
      .build();
    var pollingOptions = PollingOptions
      .builder()
      .initialDelaySec(3.0)
      .intervalSec(1.5)
      .maxRetries(80)
      .build();

    var response = mindeeClient
      .enqueueAndGetResult(SplitResponse.class, source, params, pollingOptions);
    assertNotNull(response);

    var inference = response.getInference();
    assertNotNull(inference);

    var file = inference.getFile();
    assertNotNull(file);
    assertEquals("multipage_cut-2.pdf", file.getName());
    assertEquals(2, file.getPageCount());

    assertNotNull(inference.getModel());
    assertEquals(modelId, inference.getModel().getId());

    var result = inference.getResult();
    assertNotNull(result);
    assertTrue(result.getSplits().isEmpty());
  }
}
