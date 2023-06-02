package com.mindee.parsing.custom;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.PredictResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class CustomV1Test {

  @Test
  void givenACustomDocument_whenDeserialized_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(PredictResponse.class,
      CustomV1Inference.class);
    PredictResponse<CustomV1Inference> customDocumentInference = objectMapper.readValue(
      new File("src/test/resources/custom/response_v1/complete.json"),
      type);

    String[] actualLines = customDocumentInference.getDocument().get().toString().split(System.lineSeparator());
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/custom/response_v1/summary_full.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }
}
