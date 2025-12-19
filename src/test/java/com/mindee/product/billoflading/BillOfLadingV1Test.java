package com.mindee.product.billoflading;

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
 * Unit tests for BillOfLadingV1.
 */
public class BillOfLadingV1Test {

  protected PredictResponse<BillOfLadingV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      BillOfLadingV1.class
    );
    return objectMapper.readValue(
      new File(getV1ResourcePathString("products/bill_of_lading/response_v1/" + name + ".json")),
      type
    );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<BillOfLadingV1> response = getPrediction("empty");
    BillOfLadingV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getBillOfLadingNumber().getValue());
    Assertions.assertNull(docPrediction.getShipper().getAddress());
    Assertions.assertNull(docPrediction.getShipper().getEmail());
    Assertions.assertNull(docPrediction.getShipper().getName());
    Assertions.assertNull(docPrediction.getShipper().getPhone());
    Assertions.assertNull(docPrediction.getConsignee().getAddress());
    Assertions.assertNull(docPrediction.getConsignee().getEmail());
    Assertions.assertNull(docPrediction.getConsignee().getName());
    Assertions.assertNull(docPrediction.getConsignee().getPhone());
    Assertions.assertNull(docPrediction.getNotifyParty().getAddress());
    Assertions.assertNull(docPrediction.getNotifyParty().getEmail());
    Assertions.assertNull(docPrediction.getNotifyParty().getName());
    Assertions.assertNull(docPrediction.getNotifyParty().getPhone());
    Assertions.assertNull(docPrediction.getCarrier().getName());
    Assertions.assertNull(docPrediction.getCarrier().getProfessionalNumber());
    Assertions.assertNull(docPrediction.getCarrier().getScac());
    Assertions.assertTrue(docPrediction.getCarrierItems().isEmpty());
    Assertions.assertNull(docPrediction.getPortOfLoading().getValue());
    Assertions.assertNull(docPrediction.getPortOfDischarge().getValue());
    Assertions.assertNull(docPrediction.getPlaceOfDelivery().getValue());
    Assertions.assertNull(docPrediction.getDateOfIssue().getValue());
    Assertions.assertNull(docPrediction.getDepartureDate().getValue());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<BillOfLadingV1> response = getPrediction("complete");
    Document<BillOfLadingV1> doc = response.getDocument();
    assertStringEqualsFile(
        doc.toString(),
        getV1ResourcePathString("products/bill_of_lading/response_v1/summary_full.rst")
    );
  }

}
