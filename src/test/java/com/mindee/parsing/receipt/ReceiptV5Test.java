package com.mindee.parsing.receipt;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Page;
import com.mindee.parsing.common.PredictResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Unit tests for ReceiptV5.
 */
public class ReceiptV5Test {

  protected PredictResponse<ReceiptV5Inference> getPrediction() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      ReceiptV5Inference.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/receipt/response_v5/complete.json"),
      type
    );
  }

  @Test
  void whenDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<ReceiptV5Inference> prediction = getPrediction();
    Document<ReceiptV5Inference> doc = prediction.getDocument().get();
    String[] actualLines = doc.toString().split(System.lineSeparator());
    List<String> expectedLines = Files.readAllLines(
      Paths.get("src/test/resources/receipt/response_v5/summary_full.rst")
    );
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void whenDeserialized_mustHaveValidPage0Summary() throws IOException {
    PredictResponse<ReceiptV5Inference> prediction = getPrediction();
    Page<ReceiptV5DocumentPrediction> page = prediction.getDocument().get().getInference().getPages().get(0);
    String[] actualLines = page.toString().split(System.lineSeparator());
    List<String> expectedLines = Files.readAllLines(
      Paths.get("src/test/resources/receipt/response_v5/summary_page0.rst")
    );
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }
}