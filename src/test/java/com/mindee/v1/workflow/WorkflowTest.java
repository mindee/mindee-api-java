package com.mindee.v1.workflow;

import static com.mindee.TestingUtilities.getResourcePath;
import static com.mindee.TestingUtilities.getV1ResourcePath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.input.LocalInputSource;
import com.mindee.v1.FakeMindeeApiV1;
import com.mindee.v1.MindeeClient;
import com.mindee.v1.parsing.common.Execution;
import com.mindee.v1.parsing.common.WorkflowResponse;
import com.mindee.v1.product.generated.GeneratedV1;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkflowTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    objectMapper = new ObjectMapper();
  }

  @Test
  void givenAWorkflowMockFileShouldReturnAValidWorkflowObject() throws IOException {

    var workflowResponse = new WorkflowResponse<GeneratedV1>();
    workflowResponse.setExecution(new Execution<>());
    workflowResponse.setApiRequest(null);

    var mindeeClient = new MindeeClient(new FakeMindeeApiV1<>(workflowResponse));

    WorkflowResponse<GeneratedV1> response = mindeeClient
      .executeWorkflow("", new LocalInputSource(getResourcePath("file_types/pdf/blank_1.pdf")));

    Assertions.assertNotNull(response);
  }

  @Test
  void sendingADocumentToAnExecutionShouldDeserializeResponseCorrectly() throws IOException {
    var workflowResponse = objectMapper
      .readValue(
        getV1ResourcePath("workflows/success.json").toFile(),
        WorkflowResponse.Default.class
      );
    var mindeeClient = new MindeeClient(new FakeMindeeApiV1<>(workflowResponse));

    String workflowId = "07ebf237-ff27-4eee-b6a2-425df4a5cca6";
    LocalInputSource inputSource = new LocalInputSource(
      getV1ResourcePath("products/financial_document/default_sample.jpg")
    );

    WorkflowResponse<GeneratedV1> response = mindeeClient.executeWorkflow(workflowId, inputSource);

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getApiRequest());
    Assertions.assertNull(response.getExecution().getBatchName());
    Assertions.assertNull(response.getExecution().getCreatedAt());
    Assertions.assertNull(response.getExecution().getFile().getAlias());
    Assertions.assertEquals("default_sample.jpg", response.getExecution().getFile().getName());
    Assertions
      .assertEquals("8c75c035-e083-4e77-ba3b-7c3598bd1d8a", response.getExecution().getId());
    Assertions.assertNull(response.getExecution().getInference());
    Assertions.assertEquals("medium", response.getExecution().getPriority());
    Assertions.assertNull(response.getExecution().getReviewedAt());
    Assertions.assertNull(response.getExecution().getReviewedPrediction());
    Assertions.assertEquals("processing", response.getExecution().getStatus());
    Assertions.assertEquals("manual", response.getExecution().getType());
    Assertions
      .assertEquals(
        "2024-11-13T13:02:31.699190",
        response.getExecution().getUploadedAt().toString()
      );
    Assertions.assertEquals(workflowId, response.getExecution().getWorkflowId());
  }

  @Test
  void sendingADocumentToAnExecutionWithPriorityAndAliasShouldDeserializeResponseCorrectly() throws IOException {
    var workflowResponse = objectMapper
      .readValue(
        getV1ResourcePath("workflows/success_low_priority.json").toFile(),
        WorkflowResponse.Default.class
      );

    var mindeeClient = new MindeeClient(new FakeMindeeApiV1<>(workflowResponse));

    String workflowId = "07ebf237-ff27-4eee-b6a2-425df4a5cca6";
    LocalInputSource inputSource = new LocalInputSource(
      getV1ResourcePath("products/financial_document/default_sample.jpg")
    );

    WorkflowResponse<GeneratedV1> response = mindeeClient.executeWorkflow(workflowId, inputSource);

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getApiRequest());
    Assertions.assertNull(response.getExecution().getBatchName());
    Assertions.assertNull(response.getExecution().getCreatedAt());
    Assertions
      .assertEquals("low-priority-sample-test", response.getExecution().getFile().getAlias());
    Assertions.assertEquals("default_sample.jpg", response.getExecution().getFile().getName());
    Assertions
      .assertEquals("b743e123-e18c-4b62-8a07-811a4f72afd3", response.getExecution().getId());
    Assertions.assertNull(response.getExecution().getInference());
    Assertions.assertEquals("low", response.getExecution().getPriority());
    Assertions.assertNull(response.getExecution().getReviewedAt());
    Assertions.assertNull(response.getExecution().getReviewedPrediction());
    Assertions.assertEquals("processing", response.getExecution().getStatus());
    Assertions.assertEquals("manual", response.getExecution().getType());
    Assertions
      .assertEquals(
        "2024-11-13T13:17:01.315179",
        response.getExecution().getUploadedAt().toString()
      );
    Assertions.assertEquals(workflowId, response.getExecution().getWorkflowId());
  }

}
