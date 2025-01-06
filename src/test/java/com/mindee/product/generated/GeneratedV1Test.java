package com.mindee.product.generated;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.generated.GeneratedFeature;
import com.mindee.parsing.generated.GeneratedObject;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.BooleanField;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public class GeneratedV1Test {
  protected AsyncPredictResponse<GeneratedV1> getAsyncPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      AsyncPredictResponse.class,
      GeneratedV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/generated/response_v1/" + name + "_international_id_v1.json"),
      type
    );
  }

  protected PredictResponse<GeneratedV1> getSyncPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      GeneratedV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/generated/response_v1/" + name + "_invoice_v4.json"),
      type
    );
  }

  @Test
  void whenAsyncCompleteDeserialized_mustHaveValidProperties() throws IOException {
    AsyncPredictResponse<GeneratedV1> response = getAsyncPrediction("complete");
    GeneratedV1Document docPrediction = response.getDocumentObj().getInference().getPrediction();

    Map<String, GeneratedFeature> features = docPrediction.getFields();

    // Direct access to the hashmap

    Assertions.assertFalse(features.get("address").isList());
    Assertions.assertEquals("AVDA DE MADRID S-N MADRID MADRID", features.get("address").get(0).get("value"));

    Assertions.assertFalse(features.get("birth_date").isList());
    Assertions.assertEquals("1980-01-01", features.get("birth_date").get(0).get("value"));

    Assertions.assertFalse(features.get("birth_place").isList());
    Assertions.assertEquals("MADRID", features.get("birth_place").get(0).get("value"));

    Assertions.assertFalse(features.get("country_of_issue").isList());
    Assertions.assertEquals("ESP", features.get("country_of_issue").get(0).get("value"));

    Assertions.assertFalse(features.get("document_number").isList());
    Assertions.assertEquals("99999999R", features.get("document_number").get(0).get("value"));

    Assertions.assertTrue(features.get("given_names").isList());
    Assertions.assertEquals("CARMEN", features.get("given_names").get(0).get("value"));

    Assertions.assertTrue(features.get("surnames").isList());
    Assertions.assertEquals("ESPAÑOLA", features.get("surnames").get(0).get("value"));
    Assertions.assertEquals("ESPAÑOLA", features.get("surnames").get(1).get("value"));

    // Access as a StringField without raw_value
    StringField addressField = features.get("address").asStringField();
    Assertions.assertEquals(
        "AVDA DE MADRID S-N MADRID MADRID",
        addressField.getValue()
    );
    Assertions.assertNull(addressField.getRawValue());
    Assertions.assertNull(addressField.getConfidence());
    Assertions.assertNull(addressField.getPageId());
    Assertions.assertNull(addressField.getPolygon());

    // Access as a DateField
    DateField expiryDateField = features.get("expiry_date").asDateField();
    Assertions.assertEquals(
      LocalDate.parse("2025-01-01"),
      expiryDateField.getValue()
    );
  }

  @Test
  void whenAsyncEmptyDeserialized_mustHaveValidProperties() throws IOException {
    AsyncPredictResponse<GeneratedV1> response = getAsyncPrediction("empty");
    GeneratedV1Document docPrediction = response.getDocumentObj().getInference().getPrediction();

    Map<String, GeneratedFeature> features = docPrediction.getFields();

    for (Map.Entry<String, GeneratedFeature> featureEntry : features.entrySet()) {
      GeneratedFeature featureValue = featureEntry.getValue();
      if (featureValue.isList()) {
        Assertions.assertTrue(featureValue.isEmpty());
      } else {
        Assertions.assertNull(featureValue.get(0).get("value"));
      }
    }
  }

  @Test
  void whenSyncCompleteDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<GeneratedV1> response = getSyncPrediction("complete");
    GeneratedV1Document docPrediction = response.getDocument().getInference().getPrediction();

    Map<String, GeneratedFeature> features = docPrediction.getFields();

    // Direct access to the hashmap
    GeneratedFeature customerName = features.get("customer_name");
    Assertions.assertFalse(customerName.isList());
    Assertions.assertEquals(
      "JIRO DOI",
      customerName.get(0).get("value")
    );
    Assertions.assertEquals(
      "Jiro Doi",
      customerName.get(0).get("raw_value")
    );
    Assertions.assertEquals(
      0.87,
      customerName.get(0).get("confidence")
    );
    Assertions.assertEquals(
      1,
      customerName.get(0).get("page_id")
    );
    Assertions.assertEquals(
      "Polygon with 4 points.",
      customerName.get(0).getAsPolygon("polygon").toString()
    );

    // Access as a StringField with raw_value
    StringField customerNameField = customerName.asStringField();
    Assertions.assertEquals(
      "JIRO DOI",
      customerNameField.getValue()
    );
    Assertions.assertEquals(
      "Jiro Doi",
      customerNameField.getRawValue()
    );
    Assertions.assertEquals(
      0.87,
      customerNameField.getConfidence()
    );
    Assertions.assertEquals(
      1,
      customerNameField.getPageId()
    );
    Assertions.assertEquals(
      "Polygon with 4 points.",
      customerNameField.getPolygon().toString()
    );

    // Access as a StringField without raw_value
    StringField supplierAddressField = features.get("supplier_address").asStringField();
    Assertions.assertEquals(
      "156 University Ave, Toronto ON, Canada M5H 2H7",
      supplierAddressField.getValue()
    );
    Assertions.assertNull(
      supplierAddressField.getRawValue()
    );
    Assertions.assertEquals(
      0.53,
      supplierAddressField.getConfidence()
    );
    Assertions.assertEquals(
      1,
      supplierAddressField.getPageId()
    );
    Assertions.assertEquals(
      "Polygon with 4 points.",
      supplierAddressField.getPolygon().toString()
    );

    // Access as an AmountField
    AmountField totalAmountField = features.get("total_amount").asAmountField();
    Assertions.assertEquals(
      587.95,
      totalAmountField.getValue()
    );

    // Access as a DateField
    DateField dueDateField = features.get("due_date").asDateField();
    Assertions.assertEquals(
      LocalDate.parse("2020-02-17"),
      dueDateField.getValue()
    );

    // Access as a ClassificationField
    ClassificationField documentTypeField = features.get("document_type").asClassificationField();
    Assertions.assertEquals(
      "INVOICE",
      documentTypeField.getValue()
    );

    // Access line items
    GeneratedFeature lineItems = features.get("line_items");
    Assertions.assertTrue(lineItems.isList());
    for (GeneratedObject lineItem : lineItems) {
      Assertions.assertNotNull(lineItem.get("description"));
    }
    GeneratedObject firstLineItem = lineItems.get(0);
    Assertions.assertEquals(0.84, firstLineItem.get("confidence"));
    Assertions.assertEquals("S)BOIE 5X500 FEUILLES A4", firstLineItem.get("description"));
    Assertions.assertEquals(0, firstLineItem.get("page_id"));
    Assertions.assertNull(firstLineItem.get("product_code"));
  }

  @Test
  void whenSyncEmptyDeserialized_mustHaveValidProperties() throws IOException {
    PredictResponse<GeneratedV1> response = getSyncPrediction("empty");
    GeneratedV1Document docPrediction = response.getDocument().getInference().getPrediction();

    Map<String, GeneratedFeature> features = docPrediction.getFields();

    for (Map.Entry<String, GeneratedFeature> featureEntry : features.entrySet()) {
      GeneratedFeature featureValue = featureEntry.getValue();
      if (Objects.equals(featureEntry.getKey(), "document_type")) {
        Assertions.assertEquals("INVOICE", featureValue.get(0).get("value"));
      }
      else if (featureValue.isList()) {
        Assertions.assertTrue(featureValue.isEmpty());
      } else {
        Assertions.assertNull(featureValue.get(0).get("value"));
      }
    }
  }

  @Test
  void whenAmountDeserialized_mustCastToDouble() {
    GeneratedObject intObject = new GeneratedObject();
    intObject.put("value", 5);
    AmountField fieldFromInt = intObject.asAmountField();
    Assertions.assertEquals(5.0, fieldFromInt.getValue());

    GeneratedObject doubleObject = new GeneratedObject();
    doubleObject.put("value", 5.0);
    AmountField fieldFromDouble = doubleObject.asAmountField();
    Assertions.assertEquals(5.0, fieldFromDouble.getValue());

    GeneratedObject badStringObject = new GeneratedObject();
    doubleObject.put("value", "I will fail miserably");
    Assertions.assertThrows(ClassCastException.class, badStringObject::asAmountField);
  }

  void whenBooleanDeserialized_mustCastToBoolean() {
    GeneratedObject boolObject = new GeneratedObject();
    boolObject.put("value", true);
    BooleanField booleanField = boolObject.asBooleanField();
    Assertions.assertEquals(true, booleanField.getValue());

    boolObject.put("value", false);
    booleanField = boolObject.asBooleanField();
    Assertions.assertEquals(true, booleanField.getValue());


    boolObject.put("value", null);
    booleanField = boolObject.asBooleanField();
    Assertions.assertNull(booleanField.getValue());
  }
}
