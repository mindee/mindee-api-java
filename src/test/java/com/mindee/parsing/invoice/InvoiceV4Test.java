package com.mindee.parsing.invoice;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.PredictResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


class InvoiceV4Test {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void givenAnInvoiceV4_whenDeserialized_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(PredictResponse.class, InvoiceV4Inference.class);
    PredictResponse<InvoiceV4Inference> invoiceV4Prediction =
      objectMapper.readValue(
        new File("src/test/resources/data/invoice/response_v4/complete.json"),
        type);

    List<String> lines = Files.readAllLines(Paths.get("src/test/resources/data/invoice/response_v4/summary.txt"));
    String expectedSummary = String.join("\n", lines);

    Assertions.assertNotNull(invoiceV4Prediction);
    Assertions.assertTrue(invoiceV4Prediction.getDocument().getInference().getPagesPrediction().size() > 0);
    Assertions.assertEquals(5, invoiceV4Prediction.getDocument().getInference().getDocumentPrediction().getLineItems().size());
    Assertions.assertEquals(expectedSummary, invoiceV4Prediction.getDocument().toString());
  }

}
