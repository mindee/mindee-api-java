package com.mindee.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.MindeeClientV2;
import com.mindee.input.LocalResponse;
import com.mindee.parsing.v2.DynamicField;
import com.mindee.parsing.v2.DynamicField.FieldType;
import com.mindee.parsing.v2.InferenceFields;
import com.mindee.parsing.v2.InferenceResponse;
import com.mindee.parsing.v2.ListField;
import com.mindee.parsing.v2.ObjectField;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InferenceV2 – field integrity checks")
class InferenceTest {
  @Nested
  @DisplayName("When the async prediction is blank")
  class BlankPrediction {

    @Test
    @DisplayName("all properties must be valid")
    void asyncPredict_whenEmpty_mustHaveValidProperties() throws IOException {
      MindeeClientV2 mindeeClient = new MindeeClientV2("dummy");
      InferenceResponse response = mindeeClient.loadInference(new LocalResponse(InferenceTest.class.getClassLoader().getResourceAsStream("v2/products/financial_document/blank.json")));
      InferenceFields fields = response.getInference().getResult().getFields();

      assertEquals(21, fields.size(), "Expected 21 fields");

      DynamicField taxes = fields.get("taxes");
      assertNotNull(taxes, "'taxes' field must exist");
      ListField taxesList = taxes.getListField();
      assertNotNull(taxesList, "'taxes' must be a ListField");
      assertTrue(taxesList.getItems().isEmpty(), "'taxes' list must be empty");

      DynamicField supplierAddress = fields.get("supplier_address");
      assertNotNull(supplierAddress, "'supplier_address' field must exist");
      ObjectField supplierObj = supplierAddress.getObjectField();
      assertNotNull(supplierObj, "'supplier_address' must be an ObjectField");

      for (Map.Entry<String, DynamicField> entry : fields.entrySet()) {
        DynamicField value = entry.getValue();
        if (value == null) {
          continue;
        }

        FieldType type = value.getType();
        switch (type) {
          case LIST_FIELD:
            assertNotNull(value.getListField(), entry.getKey() + " – ListField expected");
            assertNull(value.getObjectField(), entry.getKey() + " – ObjectField must be null");
            assertNull(value.getSimpleField(), entry.getKey() + " – SimpleField must be null");
            break;

          case OBJECT_FIELD:
            assertNotNull(value.getObjectField(), entry.getKey() + " – ObjectField expected");
            assertNull(value.getListField(), entry.getKey() + " – ListField must be null");
            assertNull(value.getSimpleField(), entry.getKey() + " – SimpleField must be null");
            break;

          case SIMPLE_FIELD:
          default:
            assertNotNull(value.getSimpleField(), entry.getKey() + " – SimpleField expected");
            assertNull(value.getListField(), entry.getKey() + " – ListField must be null");
            assertNull(value.getObjectField(), entry.getKey() + " – ObjectField must be null");
            break;
        }
      }
    }
  }

  @Nested
  @DisplayName("When the async prediction is complete")
  class CompletePrediction {

    @Test
    @DisplayName("all properties must be valid")
    void asyncPredict_whenComplete_mustHaveValidProperties() throws IOException {
      MindeeClientV2 mindeeClient = new MindeeClientV2("dummy");
      InferenceResponse response = mindeeClient.loadInference(new LocalResponse(InferenceTest.class.getClassLoader().getResourceAsStream("v2/products/financial_document/complete.json")));
      InferenceFields fields = response.getInference().getResult().getFields();

      assertEquals(21, fields.size(), "Expected 21 fields");

      DynamicField taxes = fields.get("taxes");
      assertNotNull(taxes, "'taxes' field must exist");
      ListField taxesList = taxes.getListField();
      assertNotNull(taxesList, "'taxes' must be a ListField");
      assertEquals(1, taxesList.getItems().size(), "'taxes' list must contain exactly one item");
      assertNotNull(taxes.toString(), "'taxes' toString() must not be null");

      ObjectField taxItemObj = taxesList.getItems().get(0).getObjectField();
      assertNotNull(taxItemObj, "First item of 'taxes' must be an ObjectField");
      assertEquals(3, taxItemObj.getFields().size(), "Tax ObjectField must contain 3 sub-fields");
      assertEquals(
          31.5,
          taxItemObj.getFields().get("base").getSimpleField().getValue(),
          "'taxes.base' value mismatch"
      );

      DynamicField supplierAddress = fields.get("supplier_address");
      assertNotNull(supplierAddress, "'supplier_address' field must exist");

      ObjectField supplierObj = supplierAddress.getObjectField();
      assertNotNull(supplierObj, "'supplier_address' must be an ObjectField");

      DynamicField country = supplierObj.getFields().get("country");
      assertNotNull(country, "'supplier_address.country' must exist");
      assertEquals("USA", country.getSimpleField().getValue());
      assertEquals("USA", country.toString());

      assertNotNull(supplierAddress.toString(), "'supplier_address'.toString() must not be null");
    }
  }
}
