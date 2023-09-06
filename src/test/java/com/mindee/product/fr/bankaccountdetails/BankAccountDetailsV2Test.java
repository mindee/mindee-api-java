package com.mindee.product.fr.bankaccountdetails;

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
 * Unit tests for BankAccountDetailsV2.
 */
public class BankAccountDetailsV2Test {

  protected PredictResponse<BankAccountDetailsV2> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      BankAccountDetailsV2.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/bank_account_details/response_v2/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<BankAccountDetailsV2> response = getPrediction("empty");
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<BankAccountDetailsV2> response = getPrediction("complete");
    Document<BankAccountDetailsV2> doc = response.getDocument();
    String[] actualLines = doc.toString().split(System.lineSeparator());
    List<String> expectedLines = Files.readAllLines(
      Paths.get("src/test/resources/products/bank_account_details/response_v2/summary_full.rst")
    );
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidPage0Summary() throws IOException {
    PredictResponse<BankAccountDetailsV2> response = getPrediction("complete");
    Page<BankAccountDetailsV2Document> page = response.getDocument().getInference().getPages().get(0);
    String[] actualLines = page.toString().split(System.lineSeparator());
    List<String> expectedLines = Files.readAllLines(
      Paths.get("src/test/resources/products/bank_account_details/response_v2/summary_page0.rst")
    );
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }
}
