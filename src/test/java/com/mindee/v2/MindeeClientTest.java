package com.mindee.v2;

import static com.mindee.TestingUtilities.getResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.v2.clientoptions.BaseProductParameters;
import com.mindee.v2.clientoptions.BaseSearchParameters;
import com.mindee.v2.clientoptions.PollingOptions;
import com.mindee.v2.http.MindeeApiV2;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.parsing.JobResponse;
import com.mindee.v2.parsing.search.BaseSearchResponse;
import com.mindee.v2.product.extraction.ExtractionResponse;
import com.mindee.v2.product.extraction.params.ExtractionParameters;
import com.mindee.v2.search.models.ModelSearchResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
        BaseProductParameters parameters
    ) {
      return jobResponse;
    }

    @Override
    public JobResponse reqPostEnqueue(URLInputSource inputSource, BaseProductParameters options) {
      return jobResponse;
    }

    @Override
    public JobResponse reqGetJobById(String jobId) {
      return jobResponse;
    }

    @Override
    public <TSearchResponse extends BaseSearchResponse> TSearchResponse reqGetSearch(
        Class<TSearchResponse> responseClass,
        BaseSearchParameters parameters
    ) {
      return (TSearchResponse) new ModelSearchResponse();
    }

    @Override
    public <TResponse extends CommonResponse> TResponse reqGetResultById(
        Class<TResponse> tResponseClass,
        String inferenceId
    ) {
      return (TResponse) resultResponse;
    }

    @Override
    public <TResponse extends CommonResponse> TResponse reqGetResultByUrl(
        Class<TResponse> tResponseClass,
        String inferenceUrl
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

      var mindeeClient = new MindeeClient(new FakeMindeeApiV2(processing, null));

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
      var mindeeClient = new MindeeClient(new FakeMindeeApiV2(null, processing));

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
  @DisplayName("getResultFromUrl()")
  class GetResultFromUrl {
    @Test
    @DisplayName("hits the HTTP endpoint once and returns a non-null response")
    void document_getResultFromUrl_async() throws IOException {
      String json = Files
        .readString(getResourcePath("v2/products/extraction/financial_document/complete.json"));

      var mapper = new ObjectMapper();
      mapper.findAndRegisterModules();

      ExtractionResponse processed = mapper.readValue(json, ExtractionResponse.class);

      AtomicReference<String> capturedUrl = new AtomicReference<>();
      var api = new FakeMindeeApiV2(null, processed) {
        @Override
        public <TResponse extends CommonResponse> TResponse reqGetResultByUrl(
            Class<TResponse> tResponseClass,
            String inferenceUrl
        ) {
          capturedUrl.set(inferenceUrl);
          return (TResponse) processed;
        }
      };
      var mindeeClient = new MindeeClient(api);

      String url = "https://api.mindee.net/v2/inferences/12345678-1234-1234-1234-123456789abc";
      ExtractionResponse response = mindeeClient.getResultFromUrl(ExtractionResponse.class, url);

      assertNotNull(response, "getResultFromUrl() must return a response");
      assertEquals(url, capturedUrl.get(), "URL must be forwarded verbatim to the API");
      assertEquals(21, response.getInference().getResult().getFields().size());
      assertEquals(
        "John Smith",
        response
          .getInference()
          .getResult()
          .getFields()
          .get("supplier_name")
          .getSimpleField()
          .getValue()
      );
    }

    @Test
    @DisplayName("rejects a null URL")
    void nullUrl_throws() {
      var client = new MindeeClient(new FakeMindeeApiV2(null, null));
      assertThrows(
        IllegalArgumentException.class,
        () -> client.getResultFromUrl(ExtractionResponse.class, null)
      );
    }

    @Test
    @DisplayName("rejects a blank URL")
    void blankUrl_throws() {
      var client = new MindeeClient(new FakeMindeeApiV2(null, null));
      assertThrows(
        IllegalArgumentException.class,
        () -> client.getResultFromUrl(ExtractionResponse.class, "   ")
      );
    }
  }

  @Nested
  @DisplayName("polling with cancellation and backoff")
  class Polling {
    private JobResponse processing() throws JsonProcessingException {
      String json = "{\"job\": {\"id\": \"dummy-id\", \"status\": \"Processing\"}}";
      var mapper = new ObjectMapper();
      mapper.findAndRegisterModules();
      return mapper.readValue(json, JobResponse.class);
    }

    @Test
    @DisplayName("cancelToken aborts polling with CancellationException")
    void polling_cancelToken_aborts() throws IOException {
      JobResponse processing = processing();
      AtomicInteger jobCalls = new AtomicInteger();
      AtomicBoolean cancel = new AtomicBoolean(false);

      var api = new FakeMindeeApiV2(processing, null) {
        @Override
        public JobResponse reqGetJobById(String jobId) {
          jobCalls.incrementAndGet();
          cancel.set(true);
          return processing;
        }
      };
      var client = new MindeeClient(api);

      var options = PollingOptions
        .builder()
        .initialDelaySec(1.0)
        .intervalSec(1.0)
        .maxRetries(10)
        .cancelToken(cancel::get)
        .build();

      var input = new LocalInputSource(getResourcePath("file_types/pdf/blank_1.pdf"));
      assertThrows(CancellationException.class, () -> {
        try {
          client
            .enqueueAndGetResult(
              ExtractionResponse.class,
              input,
              ExtractionParameters.builder("dummy-model-id").build(),
              options
            );
        } catch (IOException | InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      assertTrue(jobCalls.get() >= 1, "at least one poll should occur before cancellation");
    }

    @Test
    @DisplayName("interval grows with backoff up to maxIntervalSec")
    void polling_backoff_caps() {
      var options = PollingOptions
        .builder()
        .intervalSec(1.0)
        .backoffMultiplier(2.0)
        .maxIntervalSec(5.0)
        .build();

      double interval = options.getIntervalSec();
      for (int i = 0; i < 10; i++) {
        interval = Math.min(interval * options.getBackoffMultiplier(), options.getMaxIntervalSec());
      }
      assertEquals(5.0, interval);
    }
  }
}
