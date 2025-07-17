package com.mindee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.http.MindeeApiV2;
import com.mindee.input.LocalInputSource;
import com.mindee.input.LocalResponse;
import com.mindee.parsing.v2.CommonResponse;
import com.mindee.parsing.v2.InferenceResponse;
import com.mindee.parsing.v2.JobResponse;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("MindeeClientV2 – client / API interaction tests")
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
          new LocalInputSource(new File("src/test/resources/file_types/pdf/blank_1.pdf"));
      JobResponse response = mindeeClient.enqueue(
          input,
          InferenceParameters.builder("dummy-model-id").build()
      );

      assertNotNull(response, "enqueue() must return a response");
      verify(predictable, atMostOnce())
          .reqPostInferenceEnqueue(any(LocalInputSource.class), any(InferenceParameters.class));
    }
  }

  @Nested
  @DisplayName("parseQueued()")
  class ParseQueued {
    @Test
    @DisplayName("hits the HTTP endpoint once and returns a non-null response")
    void document_getQueued_async() throws JsonProcessingException {
      MindeeApiV2 predictable = Mockito.mock(MindeeApiV2.class);
      String json = "{\"job\": {\"id\": \"dummy-id\", \"status\": \"Processing\"}}";
      ObjectMapper mapper = new ObjectMapper();
      mapper.findAndRegisterModules();

      JobResponse processing = mapper.readValue(json, JobResponse.class);

      when(predictable.reqGetJob(anyString()))
          .thenReturn(processing);

      MindeeClientV2 mindeeClient = makeClientWithMockedApi(predictable);

      CommonResponse response = mindeeClient.pollQueue("dummy-id");
      assertNotNull(response, "parseQueued() must return a response");
      verify(predictable, atMostOnce()).reqGetInference(anyString());
    }
  }

  @Nested
  @DisplayName("loadInference()")
  class LoadInference {

    @Test
    @DisplayName("parses local JSON and exposes correct field values")
    void inference_loadsLocally() throws IOException {
      MindeeClientV2 mindeeClient = new MindeeClientV2("dummy");
      File jsonFile =
          new File("src/test/resources/v2/products/financial_document/complete.json");
      LocalResponse localResponse = new LocalResponse(jsonFile);

      InferenceResponse loaded = mindeeClient.loadInference(localResponse);

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
