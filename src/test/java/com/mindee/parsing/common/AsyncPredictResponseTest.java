package com.mindee.parsing.common;

import static com.mindee.TestingUtilities.getV1ResourcePath;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AsyncPredictResponseTest {

  private AsyncPredictResponse<InvoiceSplitterV1> loadAsyncResponse(
      Path filePath
  ) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(AsyncPredictResponse.class, InvoiceSplitterV1.class);
    return objectMapper.readValue(filePath.toFile(), type);
  }

  @Test
  void whenIssuedTimeNonUTC_deserialized_mustConvertToUTCLocal() throws JsonProcessingException {
    String json = "{\"issued_at\": \"2023-01-01T05:00:00+02:00\", \"available_at\": null, \"id\": null, \"status\": null}";

    ObjectMapper objectMapper = new ObjectMapper();

    Job job = objectMapper.readValue(json, Job.class);
    Assertions.assertEquals("2023-01-01T03:00", job.getIssuedAt().toString());
  }

  @Test
  void whenAsyncPost_returnsErrorForbidden_mustBeDeserialized() throws IOException {
    AsyncPredictResponse<InvoiceSplitterV1> response = loadAsyncResponse(
      getV1ResourcePath("async/post_fail_forbidden.json")
    );
    Assertions.assertNotNull(response);
    Assertions.assertEquals("failure", response.getApiRequest().getStatus());
    Assertions.assertEquals(403, response.getApiRequest().getStatusCode());
    Assertions.assertNull(response.getJob());
  }

  @Test
  void whenAsyncPost_returnsSuccess_mustBeDeserialized() throws IOException {
    AsyncPredictResponse<InvoiceSplitterV1> response = loadAsyncResponse(
      getV1ResourcePath("async/post_success.json")
    );
    Assertions.assertNotNull(response);
    Assertions.assertEquals("success", response.getApiRequest().getStatus());
    Assertions.assertEquals(200, response.getApiRequest().getStatusCode());
    Assertions.assertEquals("waiting", response.getJob().getStatus());
    Assertions
      .assertEquals("2023-02-16T12:33:49.602947", response.getJob().getIssuedAt().toString());
    Assertions.assertNull(response.getJob().getAvailableAt());
    Assertions.assertFalse(response.getDocument().isPresent());
  }

  @Test
  void whenAsyncGet_returnsProcessing_mustBeDeserialized() throws IOException {
    AsyncPredictResponse<InvoiceSplitterV1> response = loadAsyncResponse(
      getV1ResourcePath("async/get_processing.json")
    );
    Assertions.assertNotNull(response);
    Assertions.assertEquals("success", response.getApiRequest().getStatus());
    Assertions.assertEquals("processing", response.getJob().getStatus());
    Assertions
      .assertEquals("2023-03-16T12:33:49.602947", response.getJob().getIssuedAt().toString());
    Assertions.assertNull(response.getJob().getAvailableAt());
    Assertions.assertFalse(response.getDocument().isPresent());
  }

  @Test
  void whenAsyncGet_returnsCompleted_mustBeDeserialized() throws IOException {
    AsyncPredictResponse<InvoiceSplitterV1> response = loadAsyncResponse(
      getV1ResourcePath("async/get_completed.json")
    );
    Assertions.assertNotNull(response);
    Assertions.assertEquals("success", response.getApiRequest().getStatus());
    Assertions.assertEquals("completed", response.getJob().getStatus());
    Assertions
      .assertEquals("2023-03-21T13:52:56.326107", response.getJob().getIssuedAt().toString());
    Assertions
      .assertEquals("2023-03-21T13:53:00.990339", response.getJob().getAvailableAt().toString());
    Assertions.assertTrue(response.getDocument().isPresent());
    Assertions.assertEquals(2, response.getDocumentObj().getNPages());
  }

  @Test
  void whenAsyncGet_returnsJobFailed_mustBeDeserialized() throws IOException {
    AsyncPredictResponse<InvoiceSplitterV1> response = loadAsyncResponse(
      getV1ResourcePath("async/get_failed_job_error.json")
    );
    Assertions.assertNotNull(response);
    Assertions.assertEquals("success", response.getApiRequest().getStatus());
    Assertions.assertEquals(200, response.getApiRequest().getStatusCode());
    Assertions.assertEquals("failed", response.getJob().getStatus());
    Assertions.assertEquals("ServerError", response.getJob().getError().getCode());
    Assertions
      .assertEquals("2024-02-20T10:31:06.878599", response.getJob().getIssuedAt().toString());
    Assertions
      .assertEquals("2024-02-20T10:31:06.878599", response.getJob().getAvailableAt().toString());
    Assertions.assertFalse(response.getDocument().isPresent());
  }
}
