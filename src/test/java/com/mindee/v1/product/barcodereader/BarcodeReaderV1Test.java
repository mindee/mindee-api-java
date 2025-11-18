package com.mindee.v1.product.barcodereader;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.PredictResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static com.mindee.TestingUtilities.getV1ResourcePathString;
import static com.mindee.TestingUtilities.assertStringEqualsFile;

/**
 * Unit tests for BarcodeReaderV1.
 */
public class BarcodeReaderV1Test {

  protected PredictResponse<BarcodeReaderV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      BarcodeReaderV1.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/barcode_reader/response_v1/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<BarcodeReaderV1> response = getPrediction("empty");
    BarcodeReaderV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertTrue(docPrediction.getCodes1D().isEmpty());
    Assertions.assertTrue(docPrediction.getCodes2D().isEmpty());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<BarcodeReaderV1> response = getPrediction("complete");
    Document<BarcodeReaderV1> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/barcode_reader/response_v1/summary_full.rst")
    );
  }

}
