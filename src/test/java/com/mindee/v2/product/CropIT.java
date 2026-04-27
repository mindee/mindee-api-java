package com.mindee.v2.product;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.*;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.clientOptions.PollingOptions;
import com.mindee.v2.product.crop.CropResponse;
import com.mindee.v2.product.crop.params.CropParameters;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@DisplayName("MindeeV2 –Integration Tests - Crop")
class CropIT {

  private MindeeClient mindeeClient;
  private String modelId;

  @BeforeAll
  void setUp() {
    var apiKey = System.getenv("MINDEE_V2_API_KEY");
    modelId = System.getenv("MINDEE_V2_SE_TESTS_CROP_MODEL_ID");
    mindeeClient = new MindeeClient(apiKey);
  }

  @Test
  @DisplayName("Empty, multi-page PDF – enqueue & parse must succeed")
  void parseFile_FilledMultiPage_mustSucceed() throws IOException, InterruptedException {
    var source = new LocalInputSource(getV2ResourcePath("products/crop/multipage_sample.pdf"));
    var params = CropParameters
      .builder(modelId)
      .alias("java_integration-test_crop_multipage")
      .build();
    var pollingOptions = PollingOptions
      .builder()
      .initialDelaySec(3.0)
      .intervalSec(1.5)
      .maxRetries(80)
      .build();

    CropResponse response = mindeeClient
      .enqueueAndGetResult(CropResponse.class, source, params, pollingOptions);
    assertNotNull(response);

    var inference = response.getInference();
    assertNotNull(inference);

    var file = inference.getFile();
    assertNotNull(file);
    assertEquals("multipage_sample.pdf", file.getName());
    assertEquals(2, file.getPageCount());

    assertNotNull(inference.getModel());
    assertEquals(modelId, inference.getModel().getId());

    var result = inference.getResult();
    assertNotNull(result);
    assertEquals(5, result.getCrops().size());
    assertEquals("receipt", result.getCrops().get(0).getObjectType());
  }
}
