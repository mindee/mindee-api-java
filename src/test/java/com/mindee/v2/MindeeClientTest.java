package com.mindee.v2;

import static com.mindee.TestingUtilities.getResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.input.LocalInputSource;
import com.mindee.input.LocalResponse;
import com.mindee.input.URLInputSource;
import com.mindee.v2.clientOptions.BaseParameters;
import com.mindee.v2.http.MindeeApiV2;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.parsing.JobResponse;
import com.mindee.v2.product.extraction.ExtractionResponse;
import com.mindee.v2.product.extraction.params.ExtractionParameters;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 – Client and API Tests")
class MindeeClientTest {
  private static class FakeMindeeApiV2 extends MindeeApiV2 {
    private final JobResponse jobResponse;
    private final CommonResponse resultResponse;

    public FakeMindeeApiV2(JobResponse jobResponse, CommonResponse resultResponse) {
      super();
      this.jobResponse = jobResponse;
      this.resultResponse = resultResponse;
    }

    @Override
    public JobResponse reqPostEnqueue(
        LocalInputSource inputSource,
        BaseParameters options
    ) throws IOException {
      return jobResponse;
    }

    @Override
    public JobResponse reqPostEnqueue(
        URLInputSource inputSource,
        BaseParameters options
    ) throws IOException {
      return jobResponse;
    }

    @Override
    public JobResponse reqGetJob(String jobId) {
      return jobResponse;
    }

    @Override
    public <TResponse extends CommonResponse> TResponse reqGetResult(
        Class<TResponse> tResponseClass,
        String inferenceId
    ) {
      return (TResponse) resultResponse;
    }
  }

  @Nested
  @DisplayName("enqueue()")
  class Enqueue {
    @Test
    @DisplayName("sends exactly one HTTP call and yields a non-null response")
    void enqueue_post_async() throws IOException {
      var mindeeClient = new MindeeClient(new FakeMindeeApiV2(new JobResponse(), null));

      var input = new LocalInputSource(getResourcePath("file_types/pdf/blank_1.pdf"));
      JobResponse response = mindeeClient
        .enqueue(
          input,
          ExtractionParameters.builder("dummy-model-id").textContext("test text context").build()
        );
      assertNotNull(response, "enqueue() must return a response");
    }
  }

  @Nested
  @DisplayName("getJob()")
  class GetJob {
    @Test
    @DisplayName("hits the HTTP endpoint once and returns a non-null response")
    void document_getJob_async() throws JsonProcessingException {
      String json = "{\"job\": {\"id\": \"dummy-id\", \"status\": \"Processing\"}}";
      var mapper = new ObjectMapper();
      mapper.findAndRegisterModules();

      JobResponse processing = mapper.readValue(json, JobResponse.class);

      MindeeClient mindeeClient = new MindeeClient(new FakeMindeeApiV2(processing, null));

      JobResponse response = mindeeClient.getJob("dummy-id");
      assertNotNull(response, "getJob() must return a response");
    }
  }

  @Nested
  @DisplayName("getInference()")
  class GetExtractionInference {
    @Test
    @DisplayName("hits the HTTP endpoint once and returns a non-null response")
    void document_getResult_async() throws IOException {
      String json = Files
        .readString(getResourcePath("v2/products/extraction/financial_document/complete.json"));

      var mapper = new ObjectMapper();
      mapper.findAndRegisterModules();

      ExtractionResponse processing = mapper.readValue(json, ExtractionResponse.class);
      MindeeClient mindeeClient = new MindeeClient(new FakeMindeeApiV2(null, processing));

      ExtractionResponse response = mindeeClient
        .getResult(ExtractionResponse.class, "12345678-1234-1234-1234-123456789abc");
      assertNotNull(response, "getInference() must return a response");
      assertEquals(
        21,
        response.getInference().getResult().getFields().size(),
        "Result must have one field"
      );
      assertEquals(
        "John Smith",
        response
          .getInference()
          .getResult()
          .getFields()
          .get("supplier_name")
          .getSimpleField()
          .getValue(),
        "Result must deserialize fields properly."
      );
    }
  }

  @Nested
  @DisplayName("deserializeResponse()")
  class DeserializeResponse {

    @Test
    @DisplayName("parses local JSON and exposes correct field values")
    void inference_loadsLocally() throws IOException {
      var localResponse = new LocalResponse(
        getResourcePath("v2/products/extraction/financial_document/complete.json")
      );
      ExtractionResponse loaded = localResponse.deserializeResponse(ExtractionResponse.class);

      assertNotNull(loaded, "Loaded InferenceResponse must not be null");
      assertEquals(
        "12345678-1234-1234-1234-123456789abc",
        loaded.getInference().getModel().getId(),
        "Model Id mismatch"
      );
      assertEquals(
        "John Smith",
        loaded
          .getInference()
          .getResult()
          .getFields()
          .get("supplier_name")
          .getSimpleField()
          .getValue(),
        "Supplier name mismatch"
      );
    }
  }
}
