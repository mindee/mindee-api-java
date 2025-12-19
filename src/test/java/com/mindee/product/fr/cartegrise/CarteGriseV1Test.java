package com.mindee.product.fr.cartegrise;

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
 * Unit tests for CarteGriseV1.
 */
public class CarteGriseV1Test {

  protected PredictResponse<CarteGriseV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, CarteGriseV1.class);
    return objectMapper
      .readValue(
        new File(getV1ResourcePathString("products/carte_grise/response_v1/" + name + ".json")),
        type
      );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<CarteGriseV1> response = getPrediction("empty");
    CarteGriseV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getA().getValue());
    Assertions.assertNull(docPrediction.getB().getValue());
    Assertions.assertNull(docPrediction.getC1().getValue());
    Assertions.assertNull(docPrediction.getC3().getValue());
    Assertions.assertNull(docPrediction.getC41().getValue());
    Assertions.assertNull(docPrediction.getC4A().getValue());
    Assertions.assertNull(docPrediction.getD1().getValue());
    Assertions.assertNull(docPrediction.getD3().getValue());
    Assertions.assertNull(docPrediction.getE().getValue());
    Assertions.assertNull(docPrediction.getF1().getValue());
    Assertions.assertNull(docPrediction.getF2().getValue());
    Assertions.assertNull(docPrediction.getF3().getValue());
    Assertions.assertNull(docPrediction.getG().getValue());
    Assertions.assertNull(docPrediction.getG1().getValue());
    Assertions.assertNull(docPrediction.getI().getValue());
    Assertions.assertNull(docPrediction.getJ().getValue());
    Assertions.assertNull(docPrediction.getJ1().getValue());
    Assertions.assertNull(docPrediction.getJ2().getValue());
    Assertions.assertNull(docPrediction.getJ3().getValue());
    Assertions.assertNull(docPrediction.getP1().getValue());
    Assertions.assertNull(docPrediction.getP2().getValue());
    Assertions.assertNull(docPrediction.getP3().getValue());
    Assertions.assertNull(docPrediction.getP6().getValue());
    Assertions.assertNull(docPrediction.getQ().getValue());
    Assertions.assertNull(docPrediction.getS1().getValue());
    Assertions.assertNull(docPrediction.getS2().getValue());
    Assertions.assertNull(docPrediction.getU1().getValue());
    Assertions.assertNull(docPrediction.getU2().getValue());
    Assertions.assertNull(docPrediction.getV7().getValue());
    Assertions.assertNull(docPrediction.getX1().getValue());
    Assertions.assertNull(docPrediction.getY1().getValue());
    Assertions.assertNull(docPrediction.getY2().getValue());
    Assertions.assertNull(docPrediction.getY3().getValue());
    Assertions.assertNull(docPrediction.getY4().getValue());
    Assertions.assertNull(docPrediction.getY5().getValue());
    Assertions.assertNull(docPrediction.getY6().getValue());
    Assertions.assertNull(docPrediction.getFormulaNumber().getValue());
    Assertions.assertNull(docPrediction.getOwnerFirstName().getValue());
    Assertions.assertNull(docPrediction.getOwnerSurname().getValue());
    Assertions.assertNull(docPrediction.getMrz1().getValue());
    Assertions.assertNull(docPrediction.getMrz2().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<CarteGriseV1> response = getPrediction("complete");
    Document<CarteGriseV1> doc = response.getDocument();
    assertStringEqualsFile(
      doc.toString(),
      getV1ResourcePathString("products/carte_grise/response_v1/summary_full.rst")
    );
  }

}
