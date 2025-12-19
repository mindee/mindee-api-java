package com.mindee.product.ind.indianpassport;

import static com.mindee.TestingUtilities.assertStringEqualsFile;
import static com.mindee.TestingUtilities.getV1ResourcePathString;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.standard.ClassificationField;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for IndianPassportV1.
 */
public class IndianPassportV1Test {

  protected PredictResponse<IndianPassportV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, IndianPassportV1.class);
    return objectMapper
      .readValue(
        new File(getV1ResourcePathString("products/ind_passport/response_v1/" + name + ".json")),
        type
      );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<IndianPassportV1> response = getPrediction("empty");
    IndianPassportV1Document docPrediction = response.getDocument().getInference().getPrediction();
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
    Assertions.assertNull(docPrediction.getOldPassportPlaceOfIssue().getValue());
    Assertions.assertNull(docPrediction.getAddress1().getValue());
    Assertions.assertNull(docPrediction.getAddress2().getValue());
    Assertions.assertNull(docPrediction.getAddress3().getValue());
    Assertions.assertNull(docPrediction.getFileNumber().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<IndianPassportV1> response = getPrediction("complete");
    Document<IndianPassportV1> doc = response.getDocument();
    assertStringEqualsFile(
      doc.toString(),
      getV1ResourcePathString("products/ind_passport/response_v1/summary_full.rst")
    );
  }

}
