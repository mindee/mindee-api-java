package com.mindee.workflow;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.MindeeClient;
import com.mindee.http.MindeeApi;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.Execution;
import com.mindee.parsing.common.WorkflowResponse;
import com.mindee.pdf.PdfOperation;
import com.mindee.product.generated.GeneratedV1;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WorkflowTest {
  MindeeClient client;
  @Mock
  MindeeClient mockedClient;
  MindeeApi mindeeApi;
  PdfOperation pdfOperation;

  private ObjectMapper objectMapper;
  @BeforeEach
  public void setUp() {
    mindeeApi = Mockito.mock(MindeeApi.class);
    pdfOperation = Mockito.mock(PdfOperation.class);
    client = new MindeeClient(pdfOperation, mindeeApi);

    MockitoAnnotations.openMocks(this);
    objectMapper = new ObjectMapper();
  }

  @Test
  void givenAWorkflowMockFileShouldReturnAValidWorkflowObject()
      throws IOException {

    File file = new File("src/test/resources/file_types/pdf/blank_1.pdf");

    WorkflowResponse workflowResponse = new WorkflowResponse();
    workflowResponse.setExecution(new Execution());
    workflowResponse.setApiRequest(null);
    when(
            mindeeApi.executeWorkflowPost(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()))
        .thenReturn(workflowResponse);

    WorkflowResponse<GeneratedV1> execution = client.executeWorkflow(
        "",
        new LocalInputSource(file)
    );

    Assertions.assertNotNull(execution);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .executeWorkflowPost(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  void sendingADocumentToAnExecutionShouldDeserializeResponseCorrectly() throws IOException {
    File jsonFile = new File("src/test/resources/workflows/success.json");
    WorkflowResponse.Default mockResponse =
        objectMapper.readValue(jsonFile, WorkflowResponse.Default.class);

    // Mock the executeWorkflow method
    when(mockedClient.executeWorkflow(Mockito.anyString(), Mockito.any(LocalInputSource.class)))
        .thenReturn(mockResponse);

    // Test execution
    String workflowId = "workflow-id";
    String filePath = "src/test/resources/file_types/pdf/blank_1.pdf";
    LocalInputSource inputSource = new LocalInputSource(filePath);

    WorkflowResponse<GeneratedV1> response = mockedClient.executeWorkflow(workflowId, inputSource);

    // Assertions
    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getApiRequest());
    Assertions.assertNull(response.getExecution().getBatchName());
    Assertions.assertNull(response.getExecution().getCreatedAt());
    Assertions.assertNull(response.getExecution().getFile().getAlias());
    Assertions.assertEquals("default_sample.jpg", response.getExecution().getFile().getName());
     Assertions.assertEquals("8c75c035-e083-4e77-ba3b-7c3598bd1d8a", response.getExecution().getId());
     Assertions.assertNull(response.getExecution().getInference());
     Assertions.assertEquals("medium", response.getExecution().getPriority());
     Assertions.assertNull(response.getExecution().getReviewedAt());
     Assertions.assertNull(response.getExecution().getReviewedPrediction());
     Assertions.assertEquals("processing", response.getExecution().getStatus());
     Assertions.assertEquals("manual", response.getExecution().getType());
     Assertions.assertEquals("2024-11-13T13:02:31.699190", response.getExecution().getUploadedAt().toString());
     Assertions.assertEquals("07ebf237-ff27-4eee-b6a2-425df4a5cca6", response.getExecution().getWorkflowId());

    // Verify that executeWorkflow was called with the correct parameters
    Mockito.verify(mockedClient).executeWorkflow(workflowId, inputSource);
  }

}
