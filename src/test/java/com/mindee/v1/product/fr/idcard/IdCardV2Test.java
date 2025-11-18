package com.mindee.v1.product.fr.idcard;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Page;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.standard.ClassificationField;
import static com.mindee.TestingUtilities.assertStringEqualsFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static com.mindee.TestingUtilities.getV1ResourcePathString;

/**
 * Unit tests for IdCardV2.
 */
public class IdCardV2Test {

  protected PredictResponse<IdCardV2> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      IdCardV2.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/idcard_fr/response_v2/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<IdCardV2> response = getPrediction("empty");
    IdCardV2Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getNationality().getValue());
    Assertions.assertNull(docPrediction.getCardAccessNumber().getValue());
    Assertions.assertNull(docPrediction.getDocumentNumber().getValue());
    Assertions.assertTrue(docPrediction.getGivenNames().isEmpty());
    Assertions.assertNull(docPrediction.getSurname().getValue());
    Assertions.assertNull(docPrediction.getAlternateName().getValue());
    Assertions.assertNull(docPrediction.getBirthDate().getValue());
    Assertions.assertNull(docPrediction.getBirthPlace().getValue());
    Assertions.assertNull(docPrediction.getGender().getValue());
    Assertions.assertNull(docPrediction.getExpiryDate().getValue());
    Assertions.assertNull(docPrediction.getMrz1().getValue());
    Assertions.assertNull(docPrediction.getMrz2().getValue());
    Assertions.assertNull(docPrediction.getMrz3().getValue());
    Assertions.assertNull(docPrediction.getIssueDate().getValue());
    Assertions.assertNull(docPrediction.getAuthority().getValue());
    IdCardV2Page pagePrediction = response.getDocument().getInference().getPages().get(0).getPrediction();
    Assertions.assertInstanceOf(ClassificationField.class, pagePrediction.getDocumentType());
    Assertions.assertInstanceOf(ClassificationField.class, pagePrediction.getDocumentSide());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<IdCardV2> response = getPrediction("complete");
    Document<IdCardV2> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/idcard_fr/response_v2/summary_full.rst")
    );
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidPage0Summary() throws IOException {
    PredictResponse<IdCardV2> response = getPrediction("complete");
    Page<IdCardV2Page> page = response.getDocument().getInference().getPages().get(0);
    assertStringEqualsFile(
        page.toString(),
        getV1ResourcePathString("products/idcard_fr/response_v2/summary_page0.rst")
    );
  }
}
