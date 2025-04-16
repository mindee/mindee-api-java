package com.mindee.workflow;

import com.mindee.MindeeClient;
import com.mindee.MindeeException;
import com.mindee.WorkflowOptions;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.Execution;
import com.mindee.parsing.common.ExecutionPriority;
import com.mindee.parsing.common.WorkflowResponse;
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

  @BeforeAll
  static void clientSetUp() throws IOException {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
    currentDateTime = now.format(formatter);
    client = new MindeeClient();
    financialDocumentInputSource = new LocalInputSource(
        "src/test/resources/products/financial_document/default_sample.jpg"
    );
  }

  protected Execution<GeneratedV1> getFinancialDocumentWorkflow(String workflowId) throws
      IOException, MindeeException {

    WorkflowOptions options = WorkflowOptions.builder().alias("java-" + currentDateTime).priority(
        ExecutionPriority.LOW).rag(true).build();
    WorkflowResponse<GeneratedV1> response =
        client.executeWorkflow(workflowId, financialDocumentInputSource, options);
    return response.getExecution();
  }


  @Test
  public void givenAWorkflowIDShouldReturnACorrectWorkflowObject() throws IOException {
    Execution<GeneratedV1> execution = getFinancialDocumentWorkflow(System.getenv("WORKFLOW_ID"));

    Assertions.assertEquals("low", execution.getPriority());
    Assertions.assertEquals("java-" + currentDateTime, execution.getFile().getAlias());

  }
}
