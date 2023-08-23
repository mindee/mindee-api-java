package com.mindee.product.invoicesplitter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.PredictResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InvoiceSplitterV1Test {
  @Test
  void givenAnInvoiceSplitterResponse_whenDeserialized_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(PredictResponse.class,
        InvoiceSplitterV1.class);
    PredictResponse<InvoiceSplitterV1> splitterPrediction = objectMapper.readValue(
        new File("src/test/resources/products/invoice_splitter/response_v1/complete.json"),
        type);

    String[] actualLines = splitterPrediction.getDocument().toString().split(System.lineSeparator());
    List<String> expectedLines = Files
        .readAllLines(Paths.get("src/test/resources/products/invoice_splitter/response_v1/summary_full.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

}
