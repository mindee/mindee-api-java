package com.mindee.product.fr.bankaccountdetails;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.product.ProductTestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

/**
 * Unit tests for BankAccountDetailsV1.
 */
public class BankAccountDetailsV1Test {

  protected PredictResponse<BankAccountDetailsV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      BankAccountDetailsV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/bank_account_details/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<BankAccountDetailsV1> response = getPrediction("empty");
    BankAccountDetailsV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getIban().getValue());
    Assertions.assertNull(docPrediction.getAccountHolderName().getValue());
    Assertions.assertNull(docPrediction.getSwift().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<BankAccountDetailsV1> response = getPrediction("complete");
    Document<BankAccountDetailsV1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/bank_account_details/response_v1/summary_full.rst"
    );
  }

}
