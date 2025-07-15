package com.mindee.v2;

import com.mindee.MindeeClientV2;
import com.mindee.input.LocalResponse;
import com.mindee.parsing.v2.*;
import com.mindee.parsing.v2.field.*;
import com.mindee.parsing.v2.field.DynamicField.FieldType;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InferenceV2 – field integrity checks")
class InferenceTest {

  private InferenceResponse loadFromResource(String resourcePath) throws IOException {
    MindeeClientV2 dummyClient = new MindeeClientV2("dummy");
    return dummyClient.loadInference(new LocalResponse(InferenceTest.class.getClassLoader().getResourceAsStream(resourcePath)));
  }


  @Nested
  @DisplayName("When the async prediction is blank")
  class BlankPrediction {

    @Test
    @DisplayName("all properties must be valid")
    void asyncPredict_whenEmpty_mustHaveValidProperties() throws IOException {
      InferenceResponse response = loadFromResource("v2/products/financial_document/blank.json");
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
      InferenceResponse response = loadFromResource("v2/products/financial_document/complete.json");
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

  @Nested
  @DisplayName("deep_nested_fields.json")
  class DeepNestedFields {

    @Test
    @DisplayName("all nested structures must be typed correctly")
    void deepNestedFields_mustExposeCorrectTypes() throws IOException {
      InferenceResponse resp = loadFromResource("v2/inference/deep_nested_fields.json");
      Inference inf = resp.getInference();
      assertNotNull(inf);

      InferenceFields root = inf.getResult().getFields();
      assertNotNull(root.get("field_simple").getSimpleField());
      assertNotNull(root.get("field_object").getObjectField());

      ObjectField fieldObject = root.get("field_object").getObjectField();
      InferenceFields lvl1 = fieldObject.getFields();
      assertNotNull(lvl1.get("sub_object_list").getListField());
      assertNotNull(lvl1.get("sub_object_object").getObjectField());

      ObjectField subObjectObject = lvl1.get("sub_object_object").getObjectField();
      InferenceFields lvl2 = subObjectObject.getFields();
      assertNotNull(lvl2.get("sub_object_object_sub_object_list").getListField());

      ListField nestedList = lvl2.get("sub_object_object_sub_object_list").getListField();
      List<DynamicField> items = nestedList.getItems();
      assertFalse(items.isEmpty());
      assertNotNull(items.get(0).getObjectField());

      ObjectField firstItem = items.get(0).getObjectField();
      SimpleField deepSimple = firstItem.getFields()
          .get("sub_object_object_sub_object_list_simple").getSimpleField();
      assertEquals("value_9", deepSimple.getValue());
    }
  }

  @Nested
  @DisplayName("standard_field_types.json")
  class StandardFieldTypes {

    @Test
    @DisplayName("simple / object / list variants must be recognised")
    void standardFieldTypes_mustExposeCorrectTypes() throws IOException {
      InferenceResponse resp = loadFromResource("v2/inference/standard_field_types.json");
      Inference inf = resp.getInference();
      assertNotNull(inf);

      InferenceFields root = inf.getResult().getFields();
      assertNotNull(root.get("field_simple").getSimpleField());
      assertNotNull(root.get("field_object").getObjectField());
      assertNotNull(root.get("field_simple_list").getListField());
      assertNotNull(root.get("field_object_list").getListField());
    }
  }

  @Nested
  @DisplayName("raw_texts.json")
  class RawTexts {

    @Test
    @DisplayName("raw texts option must be parsed and exposed")
    void rawTexts_mustBeAccessible() throws IOException {
      InferenceResponse resp = loadFromResource("v2/inference/raw_texts.json");
      Inference inf = resp.getInference();
      assertNotNull(inf);

      InferenceResultOptions opts = inf.getResult().getOptions();
      assertNotNull(opts, "Options should not be null");

      List<RawText> rawTexts = opts.getRawTexts();
      assertEquals(2, rawTexts.size());

      RawText first = rawTexts.get(0);
      assertEquals(0, first.getPage());
      assertEquals("This is the raw text of the first page...", first.getContent());
    }
  }

  @Nested
  @DisplayName("complete.json – full inference response")
  class FullInference {
    @Test
    @DisplayName("complete financial-document JSON must round-trip correctly")
    void fullInferenceResponse_mustExposeEveryProperty() throws IOException {
      InferenceResponse resp = loadFromResource("v2/products/financial_document/complete.json");

      Inference inf = resp.getInference();
      assertNotNull(inf);
      assertEquals("12345678-1234-1234-1234-123456789abc", inf.getId());

      InferenceFields f = inf.getResult().getFields();

      SimpleField date = f.get("date").getSimpleField();
      assertEquals("2019-11-02", date.getValue());

      ListField taxes = f.get("taxes").getListField();
      ObjectField firstTax = taxes.getItems().get(0).getObjectField();
      SimpleField baseTax = firstTax.getFields().get("base").getSimpleField();
      assertEquals(31.5, baseTax.getValue());

      ObjectField customerAddr = f.get("customer_address").getObjectField();
      SimpleField city = customerAddr.getFields().get("city").getSimpleField();
      assertEquals("New York", city.getValue());

      InferenceResultModel model = inf.getModel();
      assertNotNull(model);
      assertEquals("12345678-1234-1234-1234-123456789abc", model.getId());

      InferenceResultFile file = inf.getFile();
      assertNotNull(file);
      assertEquals("complete.jpg", file.getName());
      assertNull(file.getAlias());

      assertNull(inf.getResult().getOptions());
    }
  }
}
