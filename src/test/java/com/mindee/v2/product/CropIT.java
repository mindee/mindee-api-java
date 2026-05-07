package com.mindee.v2.product;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.*;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.clientoptions.PollingOptions;
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
@DisplayName("MindeeV2 – Integration Tests - Crop")
class CropIT {

  private MindeeClient mindeeClient;
  private String cropModelId;
  private String cropExtractionModelId;

  @BeforeAll
  void setUp() {
    var apiKey = System.getenv("MINDEE_V2_API_KEY");
    cropModelId = System.getenv("MINDEE_V2_SE_TESTS_CROP_MODEL_ID");
    cropExtractionModelId = System.getenv("MINDEE_V2_SE_TESTS_CROP_EXTRACTION_MODEL_ID");
    mindeeClient = new MindeeClient(apiKey);
  }

  @Test
  @DisplayName("Filled, multi-page PDF – crop must succeed")
  void filledMultiPage_cropMustSucceed() throws IOException, InterruptedException {
    var source = new LocalInputSource(getV2ResourcePath("products/crop/multipage_sample.pdf"));
    var params = CropParameters
      .builder(cropModelId)
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
    assertEquals(cropModelId, inference.getModel().getId());

    var result = inference.getResult();
    assertNotNull(result);
    assertEquals(5, result.getCrops().size());
    var crop0 = result.getCrops().get(0);
    assertEquals("receipt", crop0.getObjectType());
    assertNotNull(crop0.getLocation().getPolygon());
    assertEquals(0, crop0.getLocation().getPage());
  }

  @Test
  @DisplayName("Filled image – crop and extraction must succeed")
  void filledSinglePage_extractionMustSucceed() throws IOException, InterruptedException {
    var source = new LocalInputSource(getV2ResourcePath("products/crop/default_sample.jpg"));
    var params = CropParameters
      .builder(cropExtractionModelId)
      .alias("java_integration-test_crop_multipage")
      .build();
    var pollingOptions = PollingOptions
      .builder()
      .initialDelaySec(5.0)
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
    assertEquals("default_sample.jpg", file.getName());
    assertEquals(1, file.getPageCount());

    assertNotNull(inference.getModel());
    assertEquals(cropExtractionModelId, inference.getModel().getId());

    var result = inference.getResult();
    assertNotNull(result);
    assertEquals(2, result.getCrops().size());
    var crop0 = result.getCrops().get(0);
    assertEquals("receipt", crop0.getObjectType());
    assertNotNull(crop0.getLocation().getPolygon());
    assertEquals(0, crop0.getLocation().getPage());
    var extractionResponse0 = crop0.getExtractionResponse();
    assertNotNull(extractionResponse0);
    assertEquals(
      "CHEZ ALAIN MIAM MIAM",
      extractionResponse0
        .getInference()
        .getResult()
        .getFields()
        .getSimpleField("supplier_name")
        .getValue()
    );
  }
}
