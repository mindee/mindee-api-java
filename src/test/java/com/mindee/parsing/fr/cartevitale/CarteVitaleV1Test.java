package com.mindee.parsing.fr.cartevitale;

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

public class CarteVitaleV1Test {

  @Test
  void givenCarteVitaleV1_whenDeserialized_MustHaveAValidSummary() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(PredictResponse.class,
      CarteVitaleV1Inference.class);
    PredictResponse<CarteVitaleV1Inference> prediction = objectMapper.readValue(
      new File("src/test/resources/fr/carte_vitale/response_v1/complete.json"),
      type);

    String[] actualLines = prediction.getDocument().get().toString().split(System.lineSeparator());
    List<String> expectedLines = Files
      .readAllLines(Paths.get("src/test/resources/fr/carte_vitale/response_v1/summary_full.rst"));
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }
}
