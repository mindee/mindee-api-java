package com.mindee.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.InferenceParameters;
import com.mindee.MindeeClientV2;
import com.mindee.http.MindeeApiV2;
import com.mindee.input.LocalInputSource;
import com.mindee.input.LocalResponse;
import com.mindee.parsing.v2.InferenceResponse;
import com.mindee.parsing.v2.JobResponse;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.mindee.TestingUtilities.getResourcePath;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("MindeeV2 – Client and API Tests")
class MindeeClientV2Test {
  /**
   * Creates a fully mocked MindeeClientV2.
   */
  private static MindeeClientV2 makeClientWithMockedApi(MindeeApiV2 mockedApi) {
    return new MindeeClientV2(mockedApi);
  }

  @Nested
  @DisplayName("enqueue()")
  class Enqueue {
    @Test
    @DisplayName("sends exactly one HTTP call and yields a non-null response")
    void enqueue_post_async() throws IOException {
      MindeeApiV2 predictable = Mockito.mock(MindeeApiV2.class);
      when(predictable.reqPostInferenceEnqueue(any(LocalInputSource.class), any(InferenceParameters.class)))
          .thenReturn(new JobResponse());

      MindeeClientV2 mindeeClient = makeClientWithMockedApi(predictable);

      LocalInputSource input =
          new LocalInputSource(getResourcePath("file_types/pdf/blank_1.pdf"));
      JobResponse response = mindeeClient.enqueueInference(
          input,
          InferenceParameters.builder("dummy-model-id").build()
      );

      assertNotNull(response, "enqueue() must return a response");
      verify(predictable, atMostOnce())
          .reqPostInferenceEnqueue(any(LocalInputSource.class), any(InferenceParameters.class));
    }
  }

  @Nested
  @DisplayName("getJob()")
  class GetJob {
    @Test
    @DisplayName("hits the HTTP endpoint once and returns a non-null response")
    void document_getJob_async() throws JsonProcessingException {
      MindeeApiV2 predictable = Mockito.mock(MindeeApiV2.class);
      String json = "{\"job\": {\"id\": \"dummy-id\", \"status\": \"Processing\"}}";
      ObjectMapper mapper = new ObjectMapper();
      mapper.findAndRegisterModules();

      JobResponse processing = mapper.readValue(json, JobResponse.class);

      when(predictable.reqGetJob(anyString()))
          .thenReturn(processing);

      MindeeClientV2 mindeeClient = makeClientWithMockedApi(predictable);

      JobResponse response = mindeeClient.getJob("dummy-id");
      assertNotNull(response, "getJob() must return a response");
      verify(predictable, atMostOnce()).reqGetJob(anyString());
    }
  }


  @Nested
  @DisplayName("getInference()")
  class GetInference {
    @Test
    @DisplayName("hits the HTTP endpoint once and returns a non-null response")
    void document_getInference_async() throws IOException {
      MindeeApiV2 predictable = Mockito.mock(MindeeApiV2.class);

      String json = FileUtils.readFileToString(
          getResourcePath("v2/products/financial_document/complete.json").toFile()
      );

      ObjectMapper mapper = new ObjectMapper();
      mapper.findAndRegisterModules();

      InferenceResponse processing = mapper.readValue(json, InferenceResponse.class);

      when(predictable.reqGetInference(anyString()))
          .thenReturn(processing);

      MindeeClientV2 mindeeClient = makeClientWithMockedApi(predictable);

      InferenceResponse response = mindeeClient.getInference("12345678-1234-1234-1234-123456789abc");
      assertNotNull(response, "getInference() must return a response");
      assertEquals(
          21, response.getInference().getResult().getFields().size(),
          "Result must have one field"
      );
      assertEquals(
          "John Smith",
          response.getInference().getResult().getFields().get("supplier_name").getSimpleField().getValue(),
          "Result must deserialize fields properly."
      );
      verify(predictable, atMostOnce()).reqGetInference(anyString());
    }
  }

  @Nested
  @DisplayName("deserializeResponse()")
  class DeserializeResponse {

    @Test
    @DisplayName("parses local JSON and exposes correct field values")
    void inference_loadsLocally() throws IOException {
      LocalResponse localResponse = new LocalResponse(
          getResourcePath("v2/products/financial_document/complete.json")
      );
      InferenceResponse loaded = localResponse.deserializeResponse(InferenceResponse.class);

      assertNotNull(loaded, "Loaded InferenceResponse must not be null");
      assertEquals(
          "12345678-1234-1234-1234-123456789abc",
          loaded.getInference().getModel().getId(),
          "Model Id mismatch"
      );
      assertEquals(
          "John Smith",
          loaded.getInference()
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
