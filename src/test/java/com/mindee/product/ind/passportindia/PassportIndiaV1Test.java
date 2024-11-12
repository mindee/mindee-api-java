package com.mindee.product.ind.passportindia;

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
 * Unit tests for PassportIndiaV1.
 */
public class PassportIndiaV1Test {

  protected PredictResponse<PassportIndiaV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      PassportIndiaV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/ind_passport/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<PassportIndiaV1> response = getPrediction("empty");
    PassportIndiaV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertInstanceOf(ClassificationField.class, docPrediction.getPageNumber());
    Assertions.assertNull(docPrediction.getCountry().getValue());
    Assertions.assertNull(docPrediction.getIdNumber().getValue());
    Assertions.assertNull(docPrediction.getGivenNames().getValue());
    Assertions.assertNull(docPrediction.getSurname().getValue());
    Assertions.assertNull(docPrediction.getBirthDate().getValue());
    Assertions.assertNull(docPrediction.getBirthPlace().getValue());
    Assertions.assertNull(docPrediction.getIssuancePlace().getValue());
    Assertions.assertInstanceOf(ClassificationField.class, docPrediction.getGender());
    Assertions.assertNull(docPrediction.getIssuanceDate().getValue());
    Assertions.assertNull(docPrediction.getExpiryDate().getValue());
    Assertions.assertNull(docPrediction.getMrz1().getValue());
    Assertions.assertNull(docPrediction.getMrz2().getValue());
    Assertions.assertNull(docPrediction.getLegalGuardian().getValue());
    Assertions.assertNull(docPrediction.getNameOfSpouse().getValue());
    Assertions.assertNull(docPrediction.getNameOfMother().getValue());
    Assertions.assertNull(docPrediction.getOldPassportDateOfIssue().getValue());
    Assertions.assertNull(docPrediction.getOldPassportNumber().getValue());
    Assertions.assertNull(docPrediction.getAddress1().getValue());
    Assertions.assertNull(docPrediction.getAddress2().getValue());
    Assertions.assertNull(docPrediction.getAddress3().getValue());
    Assertions.assertNull(docPrediction.getOldPassportPlaceOfIssue().getValue());
    Assertions.assertNull(docPrediction.getFileNumber().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<PassportIndiaV1> response = getPrediction("complete");
    Document<PassportIndiaV1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/ind_passport/response_v1/summary_full.rst"
    );
  }

}
