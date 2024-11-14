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
            Mockito.any()
        ))
        .thenReturn(workflowResponse);

    WorkflowResponse<GeneratedV1> execution = client.executeWorkflow(
        GeneratedV1.class,
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

    when(mockedClient.executeWorkflow(GeneratedV1.class, Mockito.anyString(),
        Mockito.any(LocalInputSource.class)
    ))
        .thenReturn(mockResponse);

    String workflowId = "07ebf237-ff27-4eee-b6a2-425df4a5cca6";
    String filePath = "src/test/resources/products/financial_document/default_sample.jpg";
    LocalInputSource inputSource = new LocalInputSource(filePath);

    WorkflowResponse<GeneratedV1> response =
        mockedClient.executeWorkflow(GeneratedV1.class, workflowId, inputSource);

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getApiRequest());
    Assertions.assertNull(response.getExecution().getBatchName());
    Assertions.assertNull(response.getExecution().getCreatedAt());
    Assertions.assertNull(response.getExecution().getFile().getAlias());
    Assertions.assertEquals("default_sample.jpg", response.getExecution().getFile().getName());
    Assertions.assertEquals(
        "8c75c035-e083-4e77-ba3b-7c3598bd1d8a", response.getExecution().getId());
    Assertions.assertNull(response.getExecution().getInference());
    Assertions.assertEquals("medium", response.getExecution().getPriority());
    Assertions.assertNull(response.getExecution().getReviewedAt());
    Assertions.assertNull(response.getExecution().getReviewedPrediction());
    Assertions.assertEquals("processing", response.getExecution().getStatus());
    Assertions.assertEquals("manual", response.getExecution().getType());
    Assertions.assertEquals(
        "2024-11-13T13:02:31.699190", response.getExecution().getUploadedAt().toString());
    Assertions.assertEquals(
        workflowId, response.getExecution().getWorkflowId());

    Mockito.verify(mockedClient).executeWorkflow(GeneratedV1.class, workflowId, inputSource);
  }


  @Test
  void sendingADocumentToAnExecutionWithPriorityAndAliasShouldDeserializeResponseCorrectly()
      throws IOException {
    File jsonFile = new File("src/test/resources/workflows/success_low_priority.json");
    WorkflowResponse.Default mockResponse =
        objectMapper.readValue(jsonFile, WorkflowResponse.Default.class);

    when(mockedClient.executeWorkflow(GeneratedV1.class, Mockito.anyString(),
        Mockito.any(LocalInputSource.class)
    ))
        .thenReturn(mockResponse);

    String workflowId = "07ebf237-ff27-4eee-b6a2-425df4a5cca6";
    String filePath = "src/test/resources/products/financial_document/default_sample.jpg";
    LocalInputSource inputSource = new LocalInputSource(filePath);

    WorkflowResponse<GeneratedV1> response =
        mockedClient.executeWorkflow(GeneratedV1.class, workflowId, inputSource);

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getApiRequest());
    Assertions.assertNull(response.getExecution().getBatchName());
    Assertions.assertNull(response.getExecution().getCreatedAt());
    Assertions.assertEquals(
        "low-priority-sample-test", response.getExecution().getFile().getAlias());
    Assertions.assertEquals("default_sample.jpg", response.getExecution().getFile().getName());
    Assertions.assertEquals(
        "b743e123-e18c-4b62-8a07-811a4f72afd3", response.getExecution().getId());
    Assertions.assertNull(response.getExecution().getInference());
    Assertions.assertEquals("low", response.getExecution().getPriority());
    Assertions.assertNull(response.getExecution().getReviewedAt());
    Assertions.assertNull(response.getExecution().getReviewedPrediction());
    Assertions.assertEquals("processing", response.getExecution().getStatus());
    Assertions.assertEquals("manual", response.getExecution().getType());
    Assertions.assertEquals(
        "2024-11-13T13:17:01.315179", response.getExecution().getUploadedAt().toString());
    Assertions.assertEquals(
        workflowId, response.getExecution().getWorkflowId());

    Mockito.verify(mockedClient).executeWorkflow(GeneratedV1.class, workflowId, inputSource);
  }

}
