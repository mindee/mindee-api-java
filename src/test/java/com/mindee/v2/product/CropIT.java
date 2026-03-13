package com.mindee.v2.product;

import static com.mindee.TestingUtilities.getResourcePath;
import static org.junit.jupiter.api.Assertions.*;

import com.mindee.AsyncPollingOptions;
import com.mindee.MindeeClientV2;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.v2.InferenceFile;
import com.mindee.v2.product.crop.CropInference;
import com.mindee.v2.product.crop.CropResponse;
import com.mindee.v2.product.crop.CropResult;
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

  private MindeeClientV2 mindeeClient;
  private String modelId;

  @BeforeAll
  void setUp() {
    String apiKey = System.getenv("MINDEE_V2_API_KEY");
    modelId = System.getenv("MINDEE_V2_SE_TESTS_CROP_MODEL_ID");
    mindeeClient = new MindeeClientV2(apiKey);
  }

  @Test
  @DisplayName("Empty, multi-page PDF – enqueue & parse must succeed")
  void parseFile_emptyMultiPage_mustSucceed() throws IOException, InterruptedException {
    LocalInputSource source = new LocalInputSource(
      getResourcePath("file_types/pdf/multipage_cut-2.pdf")
    );
    CropParameters params = CropParameters
      .builder(modelId)
      .alias("java_integration-test_crop_multipage")
      .pollingOptions(
        AsyncPollingOptions.builder().initialDelaySec(3.0).intervalSec(1.5).maxRetries(80).build()
      )
      .build();

    CropResponse response = mindeeClient.enqueueAndGetResult(CropResponse.class, source, params);
    assertNotNull(response);
    CropInference inference = response.getInference();
    assertNotNull(inference);

    InferenceFile file = inference.getFile();
    assertNotNull(file);
    assertEquals("multipage_cut-2.pdf", file.getName());
    assertEquals(2, file.getPageCount());

    assertNotNull(inference.getModel());
    assertEquals(modelId, inference.getModel().getId());

    CropResult result = inference.getResult();
    assertNotNull(result);
    assertEquals(1, result.getCrops().size());
    assertEquals("other", result.getCrops().get(0).getObjectType());
  }
}
