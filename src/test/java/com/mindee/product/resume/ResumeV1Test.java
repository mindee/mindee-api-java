package com.mindee.product.resume;

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
 * Unit tests for ResumeV1.
 */
public class ResumeV1Test {

  protected PredictResponse<ResumeV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, ResumeV1.class);
    return objectMapper
      .readValue(
        new File(getV1ResourcePathString("products/resume/response_v1/" + name + ".json")),
        type
      );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<ResumeV1> response = getPrediction("empty");
    ResumeV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getDocumentLanguage().getValue());
    Assertions.assertInstanceOf(ClassificationField.class, docPrediction.getDocumentType());
    Assertions.assertTrue(docPrediction.getGivenNames().isEmpty());
    Assertions.assertTrue(docPrediction.getSurnames().isEmpty());
    Assertions.assertNull(docPrediction.getNationality().getValue());
    Assertions.assertNull(docPrediction.getEmailAddress().getValue());
    Assertions.assertNull(docPrediction.getPhoneNumber().getValue());
    Assertions.assertNull(docPrediction.getAddress().getValue());
    Assertions.assertTrue(docPrediction.getSocialNetworksUrls().isEmpty());
    Assertions.assertNull(docPrediction.getProfession().getValue());
    Assertions.assertNull(docPrediction.getJobApplied().getValue());
    Assertions.assertTrue(docPrediction.getLanguages().isEmpty());
    Assertions.assertTrue(docPrediction.getHardSkills().isEmpty());
    Assertions.assertTrue(docPrediction.getSoftSkills().isEmpty());
    Assertions.assertTrue(docPrediction.getEducation().isEmpty());
    Assertions.assertTrue(docPrediction.getProfessionalExperiences().isEmpty());
    Assertions.assertTrue(docPrediction.getCertificates().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<ResumeV1> response = getPrediction("complete");
    Document<ResumeV1> doc = response.getDocument();
    assertStringEqualsFile(
      doc.toString(),
      getV1ResourcePathString("products/resume/response_v1/summary_full.rst")
    );
  }

}
