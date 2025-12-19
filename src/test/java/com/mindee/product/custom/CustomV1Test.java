package com.mindee.product.custom;

import static com.mindee.TestingUtilities.assertStringEqualsFile;
import static com.mindee.TestingUtilities.getV1ResourcePathString;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Page;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.custom.ListField;
import com.mindee.parsing.custom.ListFieldValue;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomV1Test {

  protected PredictResponse<CustomV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      CustomV1.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/custom/response_v1/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<CustomV1> response = getPrediction("empty");
    CustomV1Document docPrediction = response.getDocument().getInference().getPrediction();

    Assertions.assertFalse(docPrediction.getFields().isEmpty());
    for (Map.Entry<String, ListField> entry : docPrediction.getFields().entrySet()) {
      ListField field = entry.getValue();
      Assertions.assertTrue(field.getValues().isEmpty());
    }
    Assertions.assertFalse(docPrediction.getClassificationFields().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidPageId() throws IOException {
    PredictResponse<CustomV1> response = getPrediction("complete");
    CustomV1 inference = response.getDocument().getInference();
    CustomV1Document docPrediction = inference.getPrediction();
    for (Map.Entry<String, ListField> entry : docPrediction.getFields().entrySet()) {
      ListField field = entry.getValue();
      Assertions.assertFalse(field.isEmpty());
      Assertions.assertNotNull(field.getContentsList());
      for (ListFieldValue value : field.getValues()) {
        Assertions.assertNotNull(value.getContent());
        Assertions.assertNotNull(value.getPageId());
      }
    }
    Page<CustomV1Page> page = inference.getPages().get(0);
    Assertions.assertEquals(0, page.getPageId());

    CustomV1Page pagePrediction = page.getPrediction();
    Assertions.assertEquals(10, pagePrediction.size());
    for (Map.Entry<String, ListField> entry : pagePrediction.entrySet()) {
      ListField field = entry.getValue();
      for (ListFieldValue value : field.getValues()) {
        Assertions.assertNotNull(value.getContent());
      }
    }
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<CustomV1> response = getPrediction("complete");
    Document<CustomV1> doc = response.getDocument();
    assertStringEqualsFile(
      doc.toString(),
      getV1ResourcePathString("products/custom/response_v1/summary_full.rst")
    );
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidPage0Summary() throws IOException {
    PredictResponse<CustomV1> response = getPrediction("complete");
    Page<CustomV1Page> page = response.getDocument().getInference().getPages().get(0);
    assertStringEqualsFile(
      page.toString(),
      getV1ResourcePathString("products/custom/response_v1/summary_page0.rst")
    );
  }
}
