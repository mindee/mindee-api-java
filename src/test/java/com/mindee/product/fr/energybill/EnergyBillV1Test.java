package com.mindee.product.fr.energybill;

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
 * Unit tests for EnergyBillV1.
 */
public class EnergyBillV1Test {

  protected PredictResponse<EnergyBillV1> getPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, EnergyBillV1.class);
    return objectMapper
      .readValue(
        new File(getV1ResourcePathString("products/energy_bill_fra/response_v1/" + name + ".json")),
        type
      );
  }

  @Test
  void whenEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<EnergyBillV1> response = getPrediction("empty");
    EnergyBillV1Document docPrediction = response.getDocument().getInference().getPrediction();
    Assertions.assertNull(docPrediction.getInvoiceNumber().getValue());
    Assertions.assertNull(docPrediction.getContractId().getValue());
    Assertions.assertNull(docPrediction.getDeliveryPoint().getValue());
    Assertions.assertNull(docPrediction.getInvoiceDate().getValue());
    Assertions.assertNull(docPrediction.getDueDate().getValue());
    Assertions.assertNull(docPrediction.getTotalBeforeTaxes().getValue());
    Assertions.assertNull(docPrediction.getTotalTaxes().getValue());
    Assertions.assertNull(docPrediction.getTotalAmount().getValue());
    Assertions.assertNull(docPrediction.getEnergySupplier().getAddress());
    Assertions.assertNull(docPrediction.getEnergySupplier().getName());
    Assertions.assertNull(docPrediction.getEnergyConsumer().getAddress());
    Assertions.assertNull(docPrediction.getEnergyConsumer().getName());
    Assertions.assertTrue(docPrediction.getSubscription().isEmpty());
    Assertions.assertTrue(docPrediction.getEnergyUsage().isEmpty());
    Assertions.assertTrue(docPrediction.getTaxesAndContributions().isEmpty());
    Assertions.assertNull(docPrediction.getMeterDetails().getMeterNumber());
    Assertions.assertNull(docPrediction.getMeterDetails().getMeterType());
    Assertions.assertNull(docPrediction.getMeterDetails().getUnit());
  }

  @Test
  void whenCompleteDeserialized_mustHaveValidDocumentSummary() throws IOException {
    PredictResponse<EnergyBillV1> response = getPrediction("complete");
    Document<EnergyBillV1> doc = response.getDocument();
    assertStringEqualsFile(
      doc.toString(),
      getV1ResourcePathString("products/energy_bill_fra/response_v1/summary_full.rst")
    );
  }

}
