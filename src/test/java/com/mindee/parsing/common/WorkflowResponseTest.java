package com.mindee.parsing.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1;
import java.io.File;
import java.io.IOException;

public class WorkflowResponseTest {
  private WorkflowResponse<InvoiceSplitterV1> loadWorkflowResponse(String filePath) throws
      IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
        WorkflowResponse.class,
        InvoiceSplitterV1.class
    );
    return objectMapper.readValue(new File(filePath), type);
  }
}
