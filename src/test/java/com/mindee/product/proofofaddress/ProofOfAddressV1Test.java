package com.mindee.product.proofofaddress;

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
 * Unit tests for ProofOfAddressV1.
 */
public class ProofOfAddressV1Test {

  protected PredictResponse<ProofOfAddressV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      ProofOfAddressV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/proof_of_address/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<ProofOfAddressV1> response = getPrediction("empty");
    ProofOfAddressV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getLocale().getValue());
    Assertions.assertNull(docPrediction.getIssuerName().getValue());
    Assertions.assertTrue(docPrediction.getIssuerCompanyRegistration().isEmpty());
    Assertions.assertNull(docPrediction.getIssuerAddress().getValue());
    Assertions.assertNull(docPrediction.getRecipientName().getValue());
    Assertions.assertTrue(docPrediction.getRecipientCompanyRegistration().isEmpty());
    Assertions.assertNull(docPrediction.getRecipientAddress().getValue());
    Assertions.assertTrue(docPrediction.getDates().isEmpty());
    Assertions.assertNull(docPrediction.getDate().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<ProofOfAddressV1> response = getPrediction("complete");
    Document<ProofOfAddressV1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/proof_of_address/response_v1/summary_full.rst"
    );
  }

}
