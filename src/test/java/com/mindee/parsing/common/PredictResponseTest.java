package com.mindee.parsing.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.invoicesplitter.InvoiceSplitterV1Inference;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

public class PredictResponseTest extends TestCase {

  private PredictResponse<InvoiceSplitterV1Inference> loadAsyncResponse(String filePath) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      InvoiceSplitterV1Inference.class
    );
    return objectMapper.readValue(new File(filePath), type);
  }

  @Test
  void whenIssuedTimeNonUTC_deserialized_mustConvertToUTCLocal() throws JsonProcessingException {
    String json = "{\"issued_at\": \"2023-01-01T05:00:00+02:00\", \"available_at\": null, \"id\": null, \"status\": null}";

    ObjectMapper objectMapper = new ObjectMapper();

    Job job = objectMapper.readValue(json, Job.class);
    Assertions.assertEquals("2023-01-01T03:00",job.getIssuedAt().toString());

  }

  @Test
  void whenAsyncPost_returnsErrorForbidden_mustBeDeserialized() throws IOException {
    PredictResponse<InvoiceSplitterV1Inference> response = loadAsyncResponse(
        "src/test/resources/async/post_fail_forbidden.json"
    );
    Assertions.assertNotNull(response);
    Assertions.assertEquals("failure",response.getApiRequest().getStatus());
    Assertions.assertEquals(403,response.getApiRequest().getStatusCode());
    Assertions.assertNull(response.getJob().get().getStatus());
    Assertions.assertEquals("2023-01-01T00:00",response.getJob().get().getIssuedAt().toString());
    Assertions.assertNull(response.getJob().get().getAvailableAt());
  }

  @Test
  void whenAsyncPost_returnsSuccess_mustBeDeserialized() throws IOException {
    PredictResponse<InvoiceSplitterV1Inference> response = loadAsyncResponse(
      "src/test/resources/async/post_success.json"
    );
    Assertions.assertNotNull(response);
    Assertions.assertEquals("success",response.getApiRequest().getStatus());
    Assertions.assertEquals(200,response.getApiRequest().getStatusCode());
    Assertions.assertEquals("waiting",response.getJob().get().getStatus());
    Assertions.assertEquals("2023-02-16T12:33:49.602947",response.getJob().get().getIssuedAt().toString());
    Assertions.assertNull(response.getJob().get().getAvailableAt());
  }

  @Test
  void whenAsyncGet_returnsProcessing_mustBeDeserialized() throws IOException {
    PredictResponse<InvoiceSplitterV1Inference> response = loadAsyncResponse(
        "src/test/resources/async/get_processing.json"
    );
    Assertions.assertNotNull(response);
    Assertions.assertEquals("success", response.getApiRequest().getStatus());
    Assertions.assertEquals("processing",response.getJob().get().getStatus());
    Assertions.assertEquals("2023-03-16T12:33:49.602947",response.getJob().get().getIssuedAt().toString());
    Assertions.assertNull(response.getJob().get().getAvailableAt());
  }

  @Test
  void whenAsyncGet_returnsCompleted_mustBeDeserialized() throws IOException {
    PredictResponse<InvoiceSplitterV1Inference> response = loadAsyncResponse(
        "src/test/resources/async/get_completed.json"
    );
    Assertions.assertNotNull(response);
    Assertions.assertEquals(response.getApiRequest().getStatus(), "success");
    Assertions.assertEquals(response.getJob().get().getStatus(), "completed");
    Assertions.assertEquals(response.getJob().get().getIssuedAt().toString(), "2023-03-21T13:52:56.326107");
    Assertions.assertEquals(response.getJob().get().getAvailableAt().toString(), "2023-03-21T13:53:00.990339");
  }


}
