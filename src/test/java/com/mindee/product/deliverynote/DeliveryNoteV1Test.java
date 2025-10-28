package com.mindee.product.deliverynote;

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
 * Unit tests for DeliveryNoteV1.
 */
public class DeliveryNoteV1Test {

  protected PredictResponse<DeliveryNoteV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      DeliveryNoteV1.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/delivery_notes/response_v1/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<DeliveryNoteV1> response = getPrediction("empty");
    DeliveryNoteV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getDeliveryDate().getValue());
    Assertions.assertNull(docPrediction.getDeliveryNumber().getValue());
    Assertions.assertNull(docPrediction.getSupplierName().getValue());
    Assertions.assertNull(docPrediction.getSupplierAddress().getValue());
    Assertions.assertNull(docPrediction.getCustomerName().getValue());
    Assertions.assertNull(docPrediction.getCustomerAddress().getValue());
    Assertions.assertNull(docPrediction.getTotalAmount().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<DeliveryNoteV1> response = getPrediction("complete");
    Document<DeliveryNoteV1> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/delivery_notes/response_v1/summary_full.rst")
    );
  }

}
