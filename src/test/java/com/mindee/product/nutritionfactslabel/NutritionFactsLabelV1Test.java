package com.mindee.product.nutritionfactslabel;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.standard.ClassificationField;
import static com.mindee.TestingUtilities.assertStringEqualsFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static com.mindee.TestingUtilities.getV1ResourcePathString;

/**
 * Unit tests for NutritionFactsLabelV1.
 */
public class NutritionFactsLabelV1Test {

  protected PredictResponse<NutritionFactsLabelV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      NutritionFactsLabelV1.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/nutrition_facts/response_v1/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<NutritionFactsLabelV1> response = getPrediction("empty");
    NutritionFactsLabelV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getServingPerBox().getValue());
    Assertions.assertNull(docPrediction.getServingSize().getAmount());
    Assertions.assertNull(docPrediction.getServingSize().getUnit());
    Assertions.assertNull(docPrediction.getCalories().getDailyValue());
    Assertions.assertNull(docPrediction.getCalories().getPer100G());
    Assertions.assertNull(docPrediction.getCalories().getPerServing());
    Assertions.assertNull(docPrediction.getTotalFat().getDailyValue());
    Assertions.assertNull(docPrediction.getTotalFat().getPer100G());
    Assertions.assertNull(docPrediction.getTotalFat().getPerServing());
    Assertions.assertNull(docPrediction.getSaturatedFat().getDailyValue());
    Assertions.assertNull(docPrediction.getSaturatedFat().getPer100G());
    Assertions.assertNull(docPrediction.getSaturatedFat().getPerServing());
    Assertions.assertNull(docPrediction.getTransFat().getDailyValue());
    Assertions.assertNull(docPrediction.getTransFat().getPer100G());
    Assertions.assertNull(docPrediction.getTransFat().getPerServing());
    Assertions.assertNull(docPrediction.getCholesterol().getDailyValue());
    Assertions.assertNull(docPrediction.getCholesterol().getPer100G());
    Assertions.assertNull(docPrediction.getCholesterol().getPerServing());
    Assertions.assertNull(docPrediction.getTotalCarbohydrate().getDailyValue());
    Assertions.assertNull(docPrediction.getTotalCarbohydrate().getPer100G());
    Assertions.assertNull(docPrediction.getTotalCarbohydrate().getPerServing());
    Assertions.assertNull(docPrediction.getDietaryFiber().getDailyValue());
    Assertions.assertNull(docPrediction.getDietaryFiber().getPer100G());
    Assertions.assertNull(docPrediction.getDietaryFiber().getPerServing());
    Assertions.assertNull(docPrediction.getTotalSugars().getDailyValue());
    Assertions.assertNull(docPrediction.getTotalSugars().getPer100G());
    Assertions.assertNull(docPrediction.getTotalSugars().getPerServing());
    Assertions.assertNull(docPrediction.getAddedSugars().getDailyValue());
    Assertions.assertNull(docPrediction.getAddedSugars().getPer100G());
    Assertions.assertNull(docPrediction.getAddedSugars().getPerServing());
    Assertions.assertNull(docPrediction.getProtein().getDailyValue());
    Assertions.assertNull(docPrediction.getProtein().getPer100G());
    Assertions.assertNull(docPrediction.getProtein().getPerServing());
    Assertions.assertNull(docPrediction.getSodium().getDailyValue());
    Assertions.assertNull(docPrediction.getSodium().getPer100G());
    Assertions.assertNull(docPrediction.getSodium().getPerServing());
    Assertions.assertNull(docPrediction.getSodium().getUnit());
    Assertions.assertTrue(docPrediction.getNutrients().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<NutritionFactsLabelV1> response = getPrediction("complete");
    Document<NutritionFactsLabelV1> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/nutrition_facts/response_v1/summary_full.rst")
    );
  }

}
