package com.mindee.product.fr.bankaccountdetails;

import static com.mindee.TestingUtilities.assertStringEqualsFile;
import static com.mindee.TestingUtilities.getV1ResourcePathString;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for BankAccountDetailsV2.
 */
public class BankAccountDetailsV2Test {

  protected PredictResponse<BankAccountDetailsV2> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, BankAccountDetailsV2.class);
    return objectMapper
      .readValue(
        new File(
          getV1ResourcePathString("products/bank_account_details/response_v2/" + name + ".json")
        ),
        type
      );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<BankAccountDetailsV2> response = getPrediction("empty");
    BankAccountDetailsV2Document docPrediction = response
      .getDocument()
      .getInference()
      .getPrediction();
    Assertions.assertNull(docPrediction.getAccountHoldersNames().getValue());
    Assertions.assertNull(docPrediction.getBban().getBbanBankCode());
    Assertions.assertNull(docPrediction.getBban().getBbanBranchCode());
    Assertions.assertNull(docPrediction.getBban().getBbanKey());
    Assertions.assertNull(docPrediction.getBban().getBbanNumber());
    Assertions.assertNull(docPrediction.getIban().getValue());
    Assertions.assertNull(docPrediction.getSwiftCode().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<BankAccountDetailsV2> response = getPrediction("complete");
    Document<BankAccountDetailsV2> doc = response.getDocument();
    assertStringEqualsFile(
      doc.toString(),
      getV1ResourcePathString("products/bank_account_details/response_v2/summary_full.rst")
    );
  }

}
