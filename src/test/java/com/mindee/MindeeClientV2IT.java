package com.mindee;

import com.mindee.http.MindeeHttpExceptionV2;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.parsing.v2.InferenceActiveOptions;
import com.mindee.parsing.v2.InferenceResponse;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@DisplayName("MindeeClientV2 – integration tests (V2)")
class MindeeClientV2IT {

  private MindeeClientV2 mindeeClient;
  private String modelId;

  @BeforeAll
  void setUp() {
    String apiKey = System.getenv("MINDEE_V2_API_KEY");
    modelId = System.getenv("MINDEE_V2_FINDOC_MODEL_ID");
    mindeeClient = new MindeeClientV2(apiKey);
  }

  @Test
  @DisplayName("Empty, multi-page PDF – enqueue & parse must succeed")
  void parseFile_emptyMultiPage_mustSucceed() throws IOException, InterruptedException {
    LocalInputSource source = new LocalInputSource(
        new File("src/test/resources/file_types/pdf/multipage_cut-2.pdf"));

    InferenceParameters params = InferenceParameters
        .builder(modelId)
        .rag(false)
        .alias("java-integration-test")
        .pollingOptions(
            AsyncPollingOptions.builder()
                .initialDelaySec(3.0)
                .intervalSec(1.5)
                .maxRetries(80)
                .build()
        )
        .build();

    InferenceResponse response = mindeeClient.enqueueAndGetInference(source, params);

    assertNotNull(response);
    assertNotNull(response.getInference());

    assertNotNull(response.getInference().getFile());
    assertEquals("multipage_cut-2.pdf", response.getInference().getFile().getName());

    assertNotNull(response.getInference().getModel());
    assertEquals(modelId, response.getInference().getModel().getId());

    assertNotNull(response.getInference().getResult());

    InferenceActiveOptions activeOptions = response.getInference().getActiveOptions();
    assertNotNull(activeOptions);
    assertFalse(activeOptions.getRag());
    assertFalse(activeOptions.getRawText());
    assertFalse(activeOptions.getPolygon());
    assertFalse(activeOptions.getConfidence());
  }

  @Test
  @DisplayName("Filled, single-page image – enqueue & parse must succeed")
  void parseFile_filledSinglePage_mustSucceed() throws IOException, InterruptedException {
    LocalInputSource source = new LocalInputSource(
        new File("src/test/resources/products/financial_document/default_sample.jpg"));

    InferenceParameters options = InferenceParameters
        .builder(modelId)
        .rag(false)
        .alias("java-integration-test")
        .build();

    InferenceResponse response = mindeeClient.enqueueAndGetInference(source, options);

    assertNotNull(response);
    assertNotNull(response.getInference());

    assertNotNull(response.getInference().getFile());
    assertEquals("default_sample.jpg", response.getInference().getFile().getName());

    assertNotNull(response.getInference().getModel());
    assertEquals(modelId, response.getInference().getModel().getId());

    assertNotNull(response.getInference().getResult());
    assertNotNull(response.getInference().getResult().getFields());
    assertNotNull(response.getInference().getResult().getFields().get("supplier_name"));
    assertEquals(
        "John Smith",
        response.getInference()
            .getResult()
            .getFields()
            .get("supplier_name")
            .getSimpleField()
            .getValue()
    );
  }


  @Test
  @DisplayName("Invalid model ID – enqueue must raise 422")
  void invalidModel_mustThrowError() throws IOException {
    LocalInputSource source = new LocalInputSource(
        new File("src/test/resources/file_types/pdf/blank_1.pdf"));

    InferenceParameters params = InferenceParameters
        .builder("INVALID_MODEL_ID")
        .build();

    MindeeHttpExceptionV2 ex = assertThrows(
        MindeeHttpExceptionV2.class,
        () -> mindeeClient.enqueueInference(source, params)
    );
    assertEquals(422, ex.getStatus());
  }

  @Test
  @DisplayName("Invalid webhook ID – enqueue must raise 422")
  void invalidWebhook_mustThrowError() throws IOException {
    LocalInputSource source = new LocalInputSource(
        new File("src/test/resources/file_types/pdf/blank_1.pdf"));

    InferenceParameters params = InferenceParameters
        .builder(modelId)
        .webhookIds(new String[]{"INVALID_WEBHOOK_ID"})
        .build();

    MindeeHttpExceptionV2 ex = assertThrows(
        MindeeHttpExceptionV2.class,
        () -> mindeeClient.enqueueInference(source, params)
    );
    assertEquals(422, ex.getStatus());
  }

  @Test
  @DisplayName("Invalid job ID – parseQueued must raise an error")
  void invalidJob_mustThrowError() {
    MindeeHttpExceptionV2 ex = assertThrows(
        MindeeHttpExceptionV2.class,
        () -> mindeeClient.getInference("INVALID_JOB_ID")
    );
    assertEquals(422, ex.getStatus());
    assertNotNull(ex);
  }

  @Test
  @DisplayName("URL input source - A url param should not raise errors.")
  void urlInputSource_mustNotRaiseErrors() throws IOException, InterruptedException {
    URLInputSource urlSource = URLInputSource.builder(System.getenv("MINDEE_V2_SE_TESTS_BLANK_PDF_URL")).build();

    InferenceParameters options = InferenceParameters
        .builder(modelId)
        .build();

    InferenceResponse response = mindeeClient.enqueueAndGetInference(urlSource, options);

    assertNotNull(response);
    assertNotNull(response.getInference());
  }
}
