package com.mindee.workflow;

import com.mindee.MindeeClient;
import com.mindee.http.Endpoint;
import com.mindee.http.MindeeApi;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Execution;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.common.WorkflowResponse;
import com.mindee.pdf.PdfOperation;
import com.mindee.product.custom.CustomV1;
import com.mindee.product.generated.GeneratedV1;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WorkflowTest {
  MindeeClient client;
  MindeeApi mindeeApi;
  PdfOperation pdfOperation;

  @BeforeEach
  public void setUp() {
    mindeeApi = Mockito.mock(MindeeApi.class);
    pdfOperation = Mockito.mock(PdfOperation.class);
    client = new MindeeClient(pdfOperation, mindeeApi);
  }

  @Test
  void givenAWorkflowMockFileShouldReturnAValidWorkflowObject()
      throws IOException {

    File file = new File("src/test/resources/file_types/pdf/blank_1.pdf");

    WorkflowResponse predictResponse = new WorkflowResponse();
    predictResponse.setExecution(new Execution());
    predictResponse.setApiRequest(null);
    Mockito.when(
            mindeeApi.executeWorkflowPost(
                GeneratedV1.class,
                Mockito.any(),
                Mockito.any()))
        .thenReturn(predictResponse);

    WorkflowResponse<GeneratedV1> workflowResponse = client.executeWorkflow(
        "",
        new LocalInputSource(file)
    );

    Assertions.assertNotNull(workflowResponse);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predictPost(Mockito.any(), Mockito.any(), Mockito.any());
  }
}
