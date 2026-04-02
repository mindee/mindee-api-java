package com.mindee.v2;

import static com.mindee.TestingUtilities.getResourcePath;
import static com.mindee.TestingUtilities.getV2ResourcePath;
import static com.mindee.TestingUtilities.readFileAsString;
import static org.junit.jupiter.api.Assertions.*;

import com.mindee.AsyncPollingOptions;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.v2.http.MindeeHttpException;
import com.mindee.v2.parsing.inference.InferenceActiveOptions;
import com.mindee.v2.parsing.inference.InferenceFile;
import com.mindee.v2.parsing.inference.RawText;
import com.mindee.v2.parsing.inference.field.InferenceFields;
import com.mindee.v2.parsing.inference.field.SimpleField;
import com.mindee.v2.product.extraction.ExtractionInference;
import com.mindee.v2.product.extraction.ExtractionResponse;
import com.mindee.v2.product.extraction.ExtractionResult;
import com.mindee.v2.product.extraction.params.ExtractionParameters;
import java.io.IOException;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@DisplayName("MindeeV2 – Integration Tests")
class MindeeClientIT {

  private MindeeClient mindeeClient;
  private String modelId;

  @BeforeAll
  void setUp() {
    String apiKey = System.getenv("MINDEE_V2_API_KEY");
    modelId = System.getenv("MINDEE_V2_SE_TESTS_FINDOC_MODEL_ID");
    mindeeClient = new MindeeClient(apiKey);
  }

  @Test
  @DisplayName("Empty, multi-page PDF – enqueue & parse must succeed")
  void parseFile_emptyMultiPage_mustSucceed() throws IOException, InterruptedException {
    LocalInputSource source = new LocalInputSource(
      getResourcePath("file_types/pdf/multipage_cut-2.pdf")
    );
    ExtractionParameters params = ExtractionParameters
      .builder(modelId)
      .rag(false)
      .rawText(true)
      .polygon(null)
      .confidence(null)
      .alias("java-integration-test_multipage")
      .textContext(null)
      .pollingOptions(
        AsyncPollingOptions.builder().initialDelaySec(3.0).intervalSec(1.5).maxRetries(80).build()
      )
      .build();

    ExtractionResponse response = mindeeClient
      .enqueueAndGetResult(ExtractionResponse.class, source, params);
    assertNotNull(response);
    ExtractionInference inference = response.getInference();
    assertNotNull(inference);

    InferenceFile file = inference.getFile();
    assertNotNull(file);
    assertEquals("multipage_cut-2.pdf", file.getName());
    assertEquals(2, file.getPageCount());

    assertNotNull(inference.getModel());
    assertEquals(modelId, inference.getModel().getId());

    InferenceActiveOptions activeOptions = inference.getActiveOptions();
    assertNotNull(activeOptions);
    assertFalse(activeOptions.getRag());
    assertTrue(activeOptions.getRawText());
    assertFalse(activeOptions.getPolygon());
    assertFalse(activeOptions.getConfidence());

    ExtractionResult result = inference.getResult();
    assertNotNull(result);

    RawText rawText = result.getRawText();
    assertEquals(2, rawText.getPages().size());

    InferenceFields fields = result.getFields();
    assertNotNull(fields);
  }

  @Test
  @DisplayName("Filled, single-page image – enqueue & parse must succeed")
  void parseFile_filledSinglePage_mustSucceed() throws IOException, InterruptedException {
    LocalInputSource source = new LocalInputSource(
      getV2ResourcePath("products/extraction/financial_document/default_sample.jpg")
    );

    ExtractionParameters params = ExtractionParameters
      .builder(modelId)
      .rag(false)
      .alias("java-integration-test_single-page")
      .textContext("this is an invoice")
      .build();

    ExtractionResponse response = mindeeClient.enqueueAndGetInference(source, params);
    assertNotNull(response);
    ExtractionInference inference = response.getInference();
    assertNotNull(inference);

    InferenceFile file = inference.getFile();
    assertNotNull(file);
    assertEquals("default_sample.jpg", file.getName());
    assertEquals(1, file.getPageCount());

    assertNotNull(inference.getModel());
    assertEquals(modelId, inference.getModel().getId());

    InferenceActiveOptions activeOptions = inference.getActiveOptions();
    assertNotNull(activeOptions);
    assertFalse(activeOptions.getRag());
    assertFalse(activeOptions.getRawText());
    assertFalse(activeOptions.getPolygon());
    assertFalse(activeOptions.getConfidence());

    ExtractionResult result = inference.getResult();
    assertNotNull(result);

    RawText rawText = result.getRawText();
    assertNull(rawText);

    InferenceFields fields = result.getFields();
    assertNotNull(fields);

    SimpleField supplierName = fields.getSimpleField("supplier_name");
    assertNotNull(supplierName);
    assertEquals("John Smith", supplierName.getStringValue());
  }

  @Test
  @DisplayName("Data Schema Replace – enqueue & parse must succeed")
  void parseFile_dataSchemaReplace_mustSucceed() throws IOException, InterruptedException {
    LocalInputSource source = new LocalInputSource(
      getV2ResourcePath("products/extraction/financial_document/default_sample.jpg")
    );

    ExtractionParameters params = ExtractionParameters
      .builder(modelId)
      .rag(false)
      .alias("java-integration-test_data-schema-replace")
      .dataSchema(
        readFileAsString(getV2ResourcePath("products/extraction/data_schema_replace_param.json"))
      )
      .build();

    ExtractionResponse response = mindeeClient
      .enqueueAndGetResult(ExtractionResponse.class, source, params);
    assertNotNull(response);
    ExtractionInference inference = response.getInference();
    assertNotNull(inference);

    ExtractionResult result = inference.getResult();
    assertNotNull(result);

    RawText rawText = result.getRawText();
    assertNull(rawText);

    InferenceFields fields = result.getFields();
    assertNotNull(fields);

    SimpleField supplierName = fields.getSimpleField("test_replace");
    assertNotNull(supplierName);
    assertEquals("a test value", supplierName.getStringValue());
  }

  @Test
  @DisplayName("Invalid model ID – enqueue must raise 422")
  void invalidModel_mustThrowError() throws IOException {
    LocalInputSource source = new LocalInputSource(getResourcePath("file_types/pdf/blank_1.pdf"));
    ExtractionParameters params = ExtractionParameters
      .builder("INVALID_MODEL_ID")
      .textContext("this is invalid")
      .build();

    MindeeHttpException ex = assertThrows(
      MindeeHttpException.class,
      () -> mindeeClient.enqueueInference(source, params)
    );
    assertEquals(422, ex.getStatus());
  }

  @Test
  @DisplayName("Invalid webhook ID – enqueue must raise 422")
  void invalidWebhook_mustThrowError() throws IOException {
    LocalInputSource source = new LocalInputSource(getResourcePath("file_types/pdf/blank_1.pdf"));
    ExtractionParameters params = ExtractionParameters
      .builder(modelId)
      .webhookIds(new String[] { "INVALID_WEBHOOK_ID" })
      .build();

    MindeeHttpException ex = assertThrows(
      MindeeHttpException.class,
      () -> mindeeClient.enqueueInference(source, params)
    );
    assertEquals(422, ex.getStatus());
  }

  @Test
  @DisplayName("Invalid job ID – parseQueued must raise an error")
  void invalidJob_mustThrowError() {
    MindeeHttpException ex = assertThrows(
      MindeeHttpException.class,
      () -> mindeeClient.getInference("INVALID_JOB_ID")
    );
    assertEquals(422, ex.getStatus());
    assertNotNull(ex);
  }

  @Test
  @DisplayName("URL input source - A URL param should not raise errors.")
  void urlInputSource_mustNotRaiseErrors() throws IOException, InterruptedException {
    URLInputSource urlSource = URLInputSource
      .builder(System.getenv("MINDEE_V2_SE_TESTS_BLANK_PDF_URL"))
      .build();

    ExtractionParameters options = ExtractionParameters.builder(modelId).build();

    ExtractionResponse response = mindeeClient.enqueueAndGetInference(urlSource, options);

    assertNotNull(response);
    assertNotNull(response.getInference());
  }
}
