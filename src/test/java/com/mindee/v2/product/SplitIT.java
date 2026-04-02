package com.mindee.v2.product;

import static com.mindee.TestingUtilities.getResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindee.AsyncPollingOptions;
import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.parsing.inference.InferenceFile;
import com.mindee.v2.product.split.SplitInference;
import com.mindee.v2.product.split.SplitResponse;
import com.mindee.v2.product.split.SplitResult;
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
    String apiKey = System.getenv("MINDEE_V2_API_KEY");
    modelId = System.getenv("MINDEE_V2_SE_TESTS_SPLIT_MODEL_ID");
    mindeeClient = new MindeeClient(apiKey);
  }

  @Test
  @DisplayName("Empty, multi-page PDF – enqueue & parse must succeed")
  void parseFile_emptyMultiPage_mustSucceed() throws IOException, InterruptedException {
    LocalInputSource source = new LocalInputSource(
      getResourcePath("file_types/pdf/multipage_cut-2.pdf")
    );
    SplitParameters params = SplitParameters
      .builder(modelId)
      .alias("java_integration-test_split_multipage")
      .pollingOptions(
        AsyncPollingOptions.builder().initialDelaySec(3.0).intervalSec(1.5).maxRetries(80).build()
      )
      .build();

    SplitResponse response = mindeeClient.enqueueAndGetResult(SplitResponse.class, source, params);
    assertNotNull(response);
    SplitInference inference = response.getInference();
    assertNotNull(inference);

    InferenceFile file = inference.getFile();
    assertNotNull(file);
    assertEquals("multipage_cut-2.pdf", file.getName());
    assertEquals(2, file.getPageCount());

    assertNotNull(inference.getModel());
    assertEquals(modelId, inference.getModel().getId());

    SplitResult result = inference.getResult();
    assertNotNull(result);
    assertTrue(result.getSplits().isEmpty());
  }
}
