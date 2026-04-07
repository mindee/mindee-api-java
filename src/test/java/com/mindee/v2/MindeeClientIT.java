package com.mindee.v2;

import static com.mindee.TestingUtilities.getResourcePath;
import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.*;

import com.mindee.AsyncPollingOptions;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.v2.http.MindeeHttpExceptionV2;
import com.mindee.v2.product.extraction.ExtractionInference;
import com.mindee.v2.product.extraction.ExtractionResponse;
import com.mindee.v2.product.extraction.params.ExtractionParameters;
import java.io.IOException;
import java.nio.file.Files;
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
    var source = new LocalInputSource(getResourcePath("file_types/pdf/multipage_cut-2.pdf"));
    var params = ExtractionParameters
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

    var response = mindeeClient.enqueueAndGetResult(ExtractionResponse.class, source, params);
    assertNotNull(response);

    var inference = response.getInference();
    assertNotNull(inference);

    var file = inference.getFile();
    assertNotNull(file);
    assertEquals("multipage_cut-2.pdf", file.getName());
    assertEquals(2, file.getPageCount());

    assertNotNull(inference.getModel());
    assertEquals(modelId, inference.getModel().getId());

    var activeOptions = inference.getActiveOptions();
    assertNotNull(activeOptions);
    assertFalse(activeOptions.getRag());
    assertTrue(activeOptions.getRawText());
    assertFalse(activeOptions.getPolygon());
    assertFalse(activeOptions.getConfidence());

    var result = inference.getResult();
    assertNotNull(result);

    var rawText = result.getRawText();
    assertEquals(2, rawText.getPages().size());

    var fields = result.getFields();
    assertNotNull(fields);
  }

  @Test
  @DisplayName("Filled, single-page image – enqueue & parse must succeed")
  void parseFile_filledSinglePage_mustSucceed() throws IOException, InterruptedException {
    var source = new LocalInputSource(
      getV2ResourcePath("products/extraction/financial_document/default_sample.jpg")
    );

    var params = ExtractionParameters
      .builder(modelId)
      .rag(false)
      .alias("java-integration-test_single-page")
      .textContext("this is an invoice")
      .build();

    var response = mindeeClient.enqueueAndGetResult(ExtractionResponse.class, source, params);
    assertNotNull(response);

    var inference = response.getInference();
    assertNotNull(inference);

    var file = inference.getFile();
    assertNotNull(file);
    assertEquals("default_sample.jpg", file.getName());
    assertEquals(1, file.getPageCount());

    assertNotNull(inference.getModel());
    assertEquals(modelId, inference.getModel().getId());

    var activeOptions = inference.getActiveOptions();
    assertNotNull(activeOptions);
    assertFalse(activeOptions.getRag());
    assertFalse(activeOptions.getRawText());
    assertFalse(activeOptions.getPolygon());
    assertFalse(activeOptions.getConfidence());

    var result = inference.getResult();
    assertNotNull(result);

    var rawText = result.getRawText();
    assertNull(rawText);

    var fields = result.getFields();
    assertNotNull(fields);

    var supplierName = fields.getSimpleField("supplier_name");
    assertNotNull(supplierName);
    assertEquals("John Smith", supplierName.getStringValue());
  }

  @Test
  @DisplayName("Data Schema Replace – enqueue & parse must succeed")
  void parseFile_dataSchemaReplace_mustSucceed() throws IOException, InterruptedException {
    var source = new LocalInputSource(
      getV2ResourcePath("products/extraction/financial_document/default_sample.jpg")
    );

    var params = ExtractionParameters
      .builder(modelId)
      .rag(false)
      .alias("java-integration-test_data-schema-replace")
      .dataSchema(
        Files.readString(getV2ResourcePath("products/extraction/data_schema_replace_param.json"))
      )
      .build();

    var response = mindeeClient.enqueueAndGetResult(ExtractionResponse.class, source, params);
    assertNotNull(response);
    ExtractionInference inference = response.getInference();
    assertNotNull(inference);

    var result = inference.getResult();
    assertNotNull(result);

    var rawText = result.getRawText();
    assertNull(rawText);

    var fields = result.getFields();
    assertNotNull(fields);

    var supplierName = fields.getSimpleField("test_replace");
    assertNotNull(supplierName);
    assertEquals("a test value", supplierName.getStringValue());
  }

  @Test
  @DisplayName("Invalid model ID – enqueue must raise 422")
  void invalidModel_mustThrowError() throws IOException {
    var source = new LocalInputSource(getResourcePath("file_types/pdf/blank_1.pdf"));
    var params = ExtractionParameters
      .builder("INVALID_MODEL_ID")
      .textContext("this is invalid")
      .build();

    MindeeHttpExceptionV2 err = assertThrows(
      MindeeHttpExceptionV2.class,
      () -> mindeeClient.enqueue(source, params)
    );
    assertEquals(422, err.getStatus());
  }

  @Test
  @DisplayName("Invalid webhook ID – enqueue must raise 422")
  void invalidWebhook_mustThrowError() throws IOException {
    var source = new LocalInputSource(getResourcePath("file_types/pdf/blank_1.pdf"));
    var params = ExtractionParameters
      .builder(modelId)
      .webhookIds(new String[] { "INVALID_WEBHOOK_ID" })
      .build();

    MindeeHttpExceptionV2 err = assertThrows(
      MindeeHttpExceptionV2.class,
      () -> mindeeClient.enqueue(source, params)
    );
    assertEquals(422, err.getStatus());
  }

  @Test
  @DisplayName("Invalid job ID – parseQueued must raise an error")
  void invalidJob_mustThrowError() {
    MindeeHttpExceptionV2 err = assertThrows(
      MindeeHttpExceptionV2.class,
      () -> mindeeClient.getResult(ExtractionResponse.class, "INVALID_JOB_ID")
    );
    assertEquals(422, err.getStatus());
    assertNotNull(err);
  }

  @Test
  @DisplayName("URL input source - A URL param should not raise errors.")
  void urlInputSource_mustNotRaiseErrors() throws IOException, InterruptedException {
    var urlSource = URLInputSource
      .builder(System.getenv("MINDEE_V2_SE_TESTS_BLANK_PDF_URL"))
      .build();

    var options = ExtractionParameters.builder(modelId).build();

    var response = mindeeClient.enqueueAndGetResult(ExtractionResponse.class, urlSource, options);

    assertNotNull(response);
    assertNotNull(response.getInference());
  }
}
