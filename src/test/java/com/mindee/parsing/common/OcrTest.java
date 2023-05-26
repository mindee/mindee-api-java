package com.mindee.parsing.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.receipt.ReceiptV4Inference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class OcrTest {

  @Test
  void givenLicensePlatesV1_whenDeserialized_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      ReceiptV4Inference.class);
    PredictResponse<ReceiptV4Inference> prediction = objectMapper.readValue(
      new File("src/test/resources/ocr/complete_with_ocr.json"),
      type);

    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/ocr/ocr.txt"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);

    Assertions.assertEquals(expectedSummary, prediction.getDocument().getOcr().toString());
  }
}
