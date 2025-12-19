package com.mindee.workflow;

import static com.mindee.TestingUtilities.getV1ResourcePath;

import com.mindee.MindeeClient;
import com.mindee.PredictOptions;
import com.mindee.WorkflowOptions;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.Execution;
import com.mindee.parsing.common.ExecutionPriority;
import com.mindee.parsing.common.WorkflowResponse;
import com.mindee.product.financialdocument.FinancialDocumentV1;
import com.mindee.product.generated.GeneratedV1;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WorkflowIT {
  private static MindeeClient client;
  private static LocalInputSource financialDocumentInputSource;
  private static String currentDateTime;
  private static String workflowId;

  @BeforeAll
  static void clientSetUp() throws IOException {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
    currentDateTime = now.format(formatter);
    client = new MindeeClient();
    workflowId = System.getenv("WORKFLOW_ID");
    financialDocumentInputSource = new LocalInputSource(
        getV1ResourcePath("products/financial_document/default_sample.jpg")
    );
  }

  @Test
  public void givenAWorkflowIdUploadShouldReturnACorrectWorkflowObject() throws
      IOException {

    WorkflowOptions options = WorkflowOptions.builder().alias("java-" + currentDateTime).priority(
        ExecutionPriority.LOW).rag(true).build();
    WorkflowResponse<GeneratedV1> response =
        client.executeWorkflow(workflowId, financialDocumentInputSource, options);
    Execution<GeneratedV1> execution = response.getExecution();
    Assertions.assertEquals("low", execution.getPriority());
    Assertions.assertEquals("java-" + currentDateTime, execution.getFile().getAlias());
  }

  @Test
  public void GivenAWorkflowIdPredictCustomShouldPollAndNotMatchRag() throws
      IOException, InterruptedException {

    PredictOptions predictOptions = PredictOptions.builder().workflowId(workflowId).build();
    AsyncPredictResponse<FinancialDocumentV1> response = client.enqueueAndParse(
        FinancialDocumentV1.class, financialDocumentInputSource, predictOptions);
    Assertions.assertNotNull(response.getDocumentObj().toString());
    Assertions.assertNull(
        response.getDocumentObj().getInference().getExtras().getRag());
  }

  @Test
  public void GivenAWorkflowIdPredictCustomShouldPollAndMatchRag() throws
      IOException, InterruptedException {

    PredictOptions predictOptions = PredictOptions.builder().workflowId(workflowId).rag(true).build();
    AsyncPredictResponse<FinancialDocumentV1> response = client.enqueueAndParse(
        FinancialDocumentV1.class, financialDocumentInputSource, predictOptions);
    Assertions.assertNotNull(response.getDocumentObj().toString());
    Assertions.assertNotNull(
        response.getDocumentObj().getInference().getExtras().getRag());
    Assertions.assertNotNull(
        response.getDocumentObj().getInference().getExtras().getRag().getMatchingDocumentId());
  }
}
