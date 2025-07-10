package com.mindee;

import com.mindee.http.MindeeHttpExceptionV2;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.v2.InferenceResponse;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@DisplayName("MindeeClientV2 – integration tests (V2)")
class MindeeClientV2IntegrationTest {

  private MindeeClientV2 mindeeClient;
  private String modelId;

  @BeforeAll
  void setUp() {
    String apiKey = System.getenv("MINDEE_V2_API_KEY");
    modelId = System.getenv("MINDEE_V2_FINDOC_MODEL_ID");

    assumeTrue(
        apiKey != null && !apiKey.trim().isEmpty(),
        "MINDEE_V2_API_KEY env var is missing – integration tests skipped"
    );
    assumeTrue(
        modelId != null && !modelId.trim().isEmpty(),
        "MINDEE_V2_FINDOC_MODEL_ID env var is missing – integration tests skipped"
    );

    mindeeClient = new MindeeClientV2(apiKey);
  }

  @Test
  @DisplayName("Empty, multi-page PDF – enqueue & parse must succeed")
  void parseFile_emptyMultiPage_mustSucceed() throws IOException, InterruptedException {
    LocalInputSource source = new LocalInputSource(
        new File("src/test/resources/file_types/pdf/multipage_cut-2.pdf"));

    InferencePredictOptions options =
        InferencePredictOptions.builder(modelId).build();

    InferenceResponse response = mindeeClient.enqueueAndParse(source, options);

    assertNotNull(response);
    assertNotNull(response.getInference());

    assertNotNull(response.getInference().getFile());
    assertEquals("multipage_cut-2.pdf", response.getInference().getFile().getName());

    assertNotNull(response.getInference().getModel());
    assertEquals(modelId, response.getInference().getModel().getId());

    assertNotNull(response.getInference().getResult());
    assertNull(response.getInference().getResult().getOptions());
  }

  @Test
  @DisplayName("Filled, single-page image – enqueue & parse must succeed")
  void parseFile_filledSinglePage_mustSucceed() throws IOException, InterruptedException {
    LocalInputSource source = new LocalInputSource(
        new File("src/test/resources/products/financial_document/default_sample.jpg"));

    InferencePredictOptions options =
        InferencePredictOptions.builder(modelId).build();

    InferenceResponse response = mindeeClient.enqueueAndParse(source, options);

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
        new File("src/test/resources/file_types/pdf/multipage_cut-2.pdf"));

    InferencePredictOptions options =
        InferencePredictOptions.builder("INVALID MODEL ID").build();

    MindeeHttpExceptionV2 ex = assertThrows(
        MindeeHttpExceptionV2.class,
        () -> mindeeClient.enqueue(source, options)
    );
    assertEquals(422, ex.getStatus());
  }

  @Test
  @DisplayName("Invalid job ID – parseQueued must raise an error")
  void invalidJob_mustThrowError() {
    MindeeHttpExceptionV2 ex = assertThrows(
        MindeeHttpExceptionV2.class,
        () -> mindeeClient.parseQueued("not-a-valid-job-ID")
    );
    assertEquals(404, ex.getStatus());
    assertNotNull(ex);
  }
}
