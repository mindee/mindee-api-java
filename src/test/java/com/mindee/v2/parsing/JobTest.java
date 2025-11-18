package com.mindee.v2.parsing;

import com.mindee.input.LocalResponse;
import com.mindee.parsing.v2.ErrorResponse;
import com.mindee.parsing.v2.Job;
import com.mindee.parsing.v2.JobResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("MindeeV2 - Job Tests")
public class JobTest {
  private JobResponse loadJob(String filePath) throws IOException {
    LocalResponse localResponse = new LocalResponse(getV2ResourcePath(filePath));
    return localResponse.deserializeResponse(JobResponse.class);
  }

  @Nested
  @DisplayName("When the Job is OK")
  class OkTest {
    @Test
    @DisplayName("properties must be valid")
    void whenProcessing_mustHaveValidProperties() throws IOException {
      JobResponse response = loadJob("job/ok_processing.json");
      Job job = response.getJob();
      assertNotNull(job);
      assertEquals("Processing", job.getStatus());
      assertNull(job.getError());
    }
  }

  @Nested
  @DisplayName("When the Job fails")
  class FailTest {
    @Test
    @DisplayName("HTTP 422 properties must be valid")
    void when422_mustHaveValidProperties() throws IOException {
      JobResponse response = loadJob("job/fail_422.json");
      Job job = response.getJob();
      assertNotNull(job);
      ErrorResponse jobError = job.getError();
      assertNotNull(jobError);
      assertEquals(422, jobError.getStatus());
      assertEquals("Invalid fields in form", jobError.getTitle());
      assertEquals("422-001", jobError.getCode());
      assertEquals("One or more fields failed validation.", jobError.getDetail());
      assertEquals(1, jobError.getErrors().size());
    }
  }
}
