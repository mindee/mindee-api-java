package com.mindee.product.us.w9;

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
 * Unit tests for W9V1.
 */
public class W9V1Test {

  protected PredictResponse<W9V1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      W9V1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/us_w9/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<W9V1> response = getPrediction("empty");
    Page<W9V1Page> page = response.getDocument().getInference().getPages().get(0);
    Assertions.assertNull(page.getPrediction().getName().getValue());
    Assertions.assertNull(page.getPrediction().getSsn().getValue());
    Assertions.assertNull(page.getPrediction().getAddress().getValue());
    Assertions.assertNull(page.getPrediction().getCityStateZip().getValue());
    Assertions.assertNull(page.getPrediction().getBusinessName().getValue());
    Assertions.assertNull(page.getPrediction().getEin().getValue());
    Assertions.assertNull(page.getPrediction().getTaxClassification().getValue());
    Assertions.assertNull(page.getPrediction().getTaxClassificationOtherDetails().getValue());
    Assertions.assertNull(page.getPrediction().getW9RevisionDate().getValue());
    Assertions.assertTrue(page.getPrediction().getSignaturePosition().getPolygon().isEmpty());
    Assertions.assertTrue(page.getPrediction().getSignatureDatePosition().getPolygon().isEmpty());
    Assertions.assertNull(page.getPrediction().getTaxClassificationLlc().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<W9V1> response = getPrediction("complete");
    Document<W9V1> doc = response.getDocument();
    String[] actualLines = doc.toString().split(System.lineSeparator());
    List<String> expectedLines = Files.readAllLines(
      Paths.get("src/test/resources/products/us_w9/response_v1/summary_full.rst")
    );
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidPage0Summary() throws IOException {
    PredictResponse<W9V1> response = getPrediction("complete");
    Page<W9V1Page> page = response.getDocument().getInference().getPages().get(0);
    String[] actualLines = page.toString().split(System.lineSeparator());
    List<String> expectedLines = Files.readAllLines(
      Paths.get("src/test/resources/products/us_w9/response_v1/summary_page0.rst")
    );
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }
}
