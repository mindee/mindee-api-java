package com.mindee.product.us.w9;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Page;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.product.ProductTestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

/**
 * Unit tests for W9V1.
 */
public class W9V1Test {

  protected PredictResponse<W9V1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      W9V1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/us_w9/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<W9V1> response = getPrediction("empty");
    W9V1Page pagePrediction = response.getDocument().getInference().getPages().get(0).getPrediction();
    Assertions.assertNull(pagePrediction.getName().getValue());
    Assertions.assertNull(pagePrediction.getSsn().getValue());
    Assertions.assertNull(pagePrediction.getAddress().getValue());
    Assertions.assertNull(pagePrediction.getCityStateZip().getValue());
    Assertions.assertNull(pagePrediction.getBusinessName().getValue());
    Assertions.assertNull(pagePrediction.getEin().getValue());
    Assertions.assertNull(pagePrediction.getTaxClassification().getValue());
    Assertions.assertNull(pagePrediction.getTaxClassificationOtherDetails().getValue());
    Assertions.assertNull(pagePrediction.getW9RevisionDate().getValue());
    Assertions.assertEquals(pagePrediction.getSignaturePosition().toString(), "");
    Assertions.assertEquals(pagePrediction.getSignatureDatePosition().toString(), "");
    Assertions.assertNull(pagePrediction.getTaxClassificationLlc().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<W9V1> response = getPrediction("complete");
    Document<W9V1> doc = response.getDocument();
    ProductTestHelper.assertStringEqualsFile(
        doc.toString(),
        "src/test/resources/products/us_w9/response_v1/summary_full.rst"
    );
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidPage0Summary() throws IOException {
    PredictResponse<W9V1> response = getPrediction("complete");
    Page<W9V1Page> page = response.getDocument().getInference().getPages().get(0);
    ProductTestHelper.assertStringEqualsFile(
        page.toString(),
        "src/test/resources/products/us_w9/response_v1/summary_page0.rst"
    );
  }
}
