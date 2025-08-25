package com.mindee.parsing.v2;

import com.mindee.geometry.Point;
import com.mindee.geometry.Polygon;
import com.mindee.input.LocalResponse;
import com.mindee.parsing.v2.field.DynamicField;
import com.mindee.parsing.v2.field.FieldConfidence;
import com.mindee.parsing.v2.field.FieldLocation;
import com.mindee.parsing.v2.field.InferenceFields;
import com.mindee.parsing.v2.field.SimpleField;
import com.mindee.parsing.v2.field.ListField;
import com.mindee.parsing.v2.field.ObjectField;
import com.mindee.parsing.v2.field.DynamicField.FieldType;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InferenceV2 – field integrity checks")
class InferenceTest {

  private InferenceResponse loadFromResource(String resourcePath) throws IOException {
    LocalResponse localResponse = new LocalResponse(InferenceTest.class.getClassLoader().getResourceAsStream(resourcePath));
    return localResponse.deserializeResponse(InferenceResponse.class);
  }

  private String readFileAsString(String path)
      throws IOException
  {
    byte[] encoded = IOUtils.toByteArray(Objects.requireNonNull(InferenceTest.class.getClassLoader().getResourceAsStream(path)));
    return new String(encoded);
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
            assertThrows(IllegalStateException.class, value::getSimpleField);
            assertThrows(IllegalStateException.class, value::getObjectField);
            break;
          case OBJECT_FIELD:
            assertNotNull(value.getObjectField(), entry.getKey() + " – ObjectField expected");
            assertThrows(IllegalStateException.class, value::getSimpleField);
            assertThrows(IllegalStateException.class, value::getListField);
            break;
          case SIMPLE_FIELD:
          default:
            assertNotNull(value.getSimpleField(), entry.getKey() + " – SimpleField expected");
            assertThrows(IllegalStateException.class, value::getListField);
            assertThrows(IllegalStateException.class, value::getObjectField);
            break;
        }
      }
    }
  }

  @Nested
  @DisplayName("When the async prediction is complete")
  class CompletePrediction {

    @Test
    @DisplayName("every exposed property must be valid and consistent")
    void asyncPredict_whenComplete_mustExposeAllProperties() throws IOException {
      InferenceResponse response = loadFromResource("v2/products/financial_document/complete.json");
      Inference inf = response.getInference();
      assertNotNull(inf, "Inference must not be null");
      assertEquals("12345678-1234-1234-1234-123456789abc", inf.getId(), "Inference ID mismatch");

      InferenceModel model = inf.getModel();
      assertNotNull(model, "Model must not be null");
      assertEquals("12345678-1234-1234-1234-123456789abc", model.getId(), "Model ID mismatch");

      InferenceFile file = inf.getFile();
      assertNotNull(file, "File must not be null");
      assertEquals("complete.jpg", file.getName(), "File name mismatch");
      assertEquals(1, file.getPageCount(), "Page count mismatch");
      assertEquals("image/jpeg", file.getMimeType(), "MIME type mismatch");
      assertNull(file.getAlias(), "File alias must be null for this payload");

      InferenceFields fields = inf.getResult().getFields();
      assertEquals(21, fields.size(), "Expected 21 fields in the payload");

      SimpleField date = fields.get("date").getSimpleField();
      assertEquals("2019-11-02", date.getValue(), "'date' value mismatch");

      DynamicField taxes = fields.get("taxes");
      assertNotNull(taxes, "'taxes' field must exist");
      ListField taxesList = taxes.getListField();
      assertNotNull(taxesList, "'taxes' must be a ListField");
      assertEquals(1, taxesList.getItems().size(), "'taxes' list must contain exactly one item");
      ObjectField taxItemObj = taxesList.getItems().get(0).getObjectField();
      assertNotNull(taxItemObj, "First item of 'taxes' must be an ObjectField");
      assertEquals(3, taxItemObj.getFields().size(), "Tax ObjectField must contain 3 sub-fields");
      SimpleField baseTax = taxItemObj.getFields().get("base").getSimpleField();
      assertEquals(31.5, baseTax.getValue(), "'taxes.base' value mismatch");
      assertNotNull(taxes.toString(), "'taxes'.toString() must not be null");

      DynamicField supplierAddress = fields.get("supplier_address");
      assertNotNull(supplierAddress, "'supplier_address' field must exist");
      ObjectField supplierObj = supplierAddress.getObjectField();
      assertNotNull(supplierObj, "'supplier_address' must be an ObjectField");
      DynamicField country = supplierObj.getFields().get("country");
      assertNotNull(country, "'supplier_address.country' must exist");
      assertEquals("USA", country.getSimpleField().getValue(), "Country mismatch");
      assertEquals("USA", country.toString(), "'country'.toString() mismatch");
      assertNotNull(supplierAddress.toString(), "'supplier_address'.toString() must not be null");

      ObjectField customerAddr = fields.get("customer_address").getObjectField();
      SimpleField city = customerAddr.getFields().get("city").getSimpleField();
      assertEquals("New York", city.getValue(), "City mismatch");

      assertNull(inf.getResult().getOptions(), "Options must be null");
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
    @DisplayName("simple fields must be recognised")
    void standardFieldTypes_mustExposeSimpleFieldValues() throws IOException {
      InferenceResponse response = loadFromResource("v2/inference/standard_field_types.json");
      Inference inference = response.getInference();
      assertNotNull(inference);

      InferenceFields fields = inference.getResult().getFields();

      assertNotNull(fields.get("field_simple_string").getSimpleField());

      SimpleField fieldSimpleString = fields.get("field_simple_string").getSimpleField();
      assertNotNull(fieldSimpleString);
      assertInstanceOf(String.class, fieldSimpleString.getValue());
      assertEquals(fieldSimpleString.getValue(), fieldSimpleString.getStringValue());
      assertThrows(ClassCastException.class, fieldSimpleString::getDoubleValue);
      assertThrows(ClassCastException.class, fieldSimpleString::getBooleanValue);
      assertEquals(FieldConfidence.Certain, fieldSimpleString.getConfidence());
      assertInstanceOf(List.class, fieldSimpleString.getLocations());
      assertEquals(1, fieldSimpleString.getLocations().size());

      SimpleField fieldSimpleFloat = fields.get("field_simple_float").getSimpleField();
      assertNotNull(fieldSimpleFloat);
      assertInstanceOf(Double.class, fieldSimpleFloat.getValue());
      assertEquals(fieldSimpleFloat.getValue(), fieldSimpleFloat.getDoubleValue());
      assertThrows(ClassCastException.class, fieldSimpleFloat::getStringValue);
      assertThrows(ClassCastException.class, fieldSimpleFloat::getBooleanValue);
      assertEquals(FieldConfidence.High, fieldSimpleFloat.getConfidence());

      SimpleField fieldSimpleInt = fields.get("field_simple_int").getSimpleField();
      assertNotNull(fieldSimpleInt);
      assertInstanceOf(Double.class, fieldSimpleInt.getValue());
      assertEquals(fieldSimpleInt.getValue(), fieldSimpleInt.getDoubleValue());
      assertEquals(FieldConfidence.Medium, fieldSimpleInt.getConfidence());

      SimpleField fieldSimpleZero = fields.get("field_simple_zero").getSimpleField();
      assertNotNull(fieldSimpleZero);
      assertEquals(FieldConfidence.Low, fieldSimpleZero.getConfidence());
      assertInstanceOf(Double.class, fieldSimpleZero.getValue());

      SimpleField fieldSimpleBool = fields.get("field_simple_bool").getSimpleField();
      assertNotNull(fieldSimpleBool);
      assertInstanceOf(Boolean.class, fieldSimpleBool.getValue());
      assertEquals(fieldSimpleBool.getValue(), fieldSimpleBool.getBooleanValue());
      assertThrows(ClassCastException.class, fieldSimpleBool::getStringValue);
      assertThrows(ClassCastException.class, fieldSimpleBool::getDoubleValue);

      SimpleField fieldSimpleNull = fields.get("field_simple_null").getSimpleField();
      assertNotNull(fieldSimpleNull);
      assertNull(fieldSimpleNull.getValue());
      assertNull(fieldSimpleNull.getStringValue());
      assertNull(fieldSimpleNull.getBooleanValue());
      assertNull(fieldSimpleNull.getDoubleValue());
    }

    @Test
    @DisplayName("simple list fields must be recognised")
    void standardFieldTypes_mustExposeSimpleListFieldValues() throws IOException {
      InferenceResponse response = loadFromResource("v2/inference/standard_field_types.json");
      Inference inference = response.getInference();
      assertNotNull(inference);

      InferenceFields fields = inference.getResult().getFields();

      ListField listField = fields.get("field_simple_list").getListField();
      assertNotNull(listField);

      // Low level (dynamic) access
      List<DynamicField> dynamicItems = listField.getItems();
      assertEquals(2, dynamicItems.size());
      SimpleField firstDynamicItem = dynamicItems.get(0).getSimpleField();
      assertNotNull(firstDynamicItem);
      assertEquals(FieldConfidence.Medium, firstDynamicItem.getConfidence());
      assertInstanceOf(String.class, firstDynamicItem.getValue());
      for (DynamicField item : dynamicItems) {
        SimpleField itemField = item.getSimpleField();
        assertInstanceOf(String.class, itemField.getValue());
        assertEquals(itemField.getValue(), itemField.getStringValue());
        assertEquals(1, itemField.getLocations().size());
      }

      // High level (typed) access
      List<SimpleField> simpleItems = listField.getSimpleItems();
      assertEquals(2, simpleItems.size());
      SimpleField firstSimpleItem = simpleItems.get(0);
      assertEquals(FieldConfidence.Medium, firstSimpleItem.getConfidence());
      for (SimpleField itemField : simpleItems) {
        assertInstanceOf(String.class, itemField.getValue());
        assertEquals(itemField.getValue(), itemField.getStringValue());
        assertEquals(1, itemField.getLocations().size());
      }

      assertThrows(IllegalStateException.class, listField::getObjectItems);
    }

    @Test
    @DisplayName("object list fields must be recognised")
    void standardFieldTypes_mustExposeObjectListFieldValues() throws IOException {
      InferenceResponse response = loadFromResource("v2/inference/standard_field_types.json");
      Inference inference = response.getInference();
      assertNotNull(inference);

      InferenceFields fields = inference.getResult().getFields();

      ListField listField = fields.get("field_object_list").getListField();
      assertNotNull(listField);

      List<DynamicField> dynamicItems = listField.getItems();
      assertEquals(2, dynamicItems.size());
      ObjectField firstDynamicItem = dynamicItems.get(0).getObjectField();
      assertEquals(
          FieldConfidence.Low,
          firstDynamicItem.getFields().get("subfield_1").getSimpleField().getConfidence()
      );
      for (DynamicField item : dynamicItems) {
        ObjectField itemField = item.getObjectField();
        assertNotNull(itemField);
        InferenceFields itemFields = itemField.getFields();
        assertEquals(2, itemFields.size());
      }

      List<ObjectField> objectItems = listField.getObjectItems();
      assertEquals(2, objectItems.size());
      ObjectField firstObjectItem = objectItems.get(0);
      assertEquals(
          FieldConfidence.Low,
          firstObjectItem.getSimpleFields().get("subfield_1").getConfidence()
      );
      for (ObjectField itemField : objectItems) {
        assertNotNull(itemField);
        HashMap<String, SimpleField> itemFields = itemField.getSimpleFields();
        assertEquals(2, itemFields.size());
        InferenceFields itemSubFields = itemField.getFields();
        SimpleField itemSubfield1 = itemSubFields.getSimpleField("subfield_1");
        assertInstanceOf(String.class, itemSubfield1.getValue());
        for (Map.Entry<String, SimpleField> entry : itemFields.entrySet()) {
            SimpleField subfield = entry.getValue();
            assertEquals(subfield.getValue(), subfield.getStringValue());
        }
      }

      assertThrows(IllegalStateException.class, listField::getSimpleItems);
    }

    @Test
    @DisplayName("simple / object / list variants must be recognised")
    void standardFieldTypes_mustExposeObjectFieldValues() throws IOException {
      InferenceResponse response = loadFromResource("v2/inference/standard_field_types.json");
      Inference inference = response.getInference();
      assertNotNull(inference);

      InferenceFields fields = inference.getResult().getFields();

      ObjectField fieldObject = fields.get("field_object").getObjectField();
      assertNotNull(fieldObject);

      InferenceFields subFieldsDynamic = fieldObject.getFields();
      assertEquals(2, subFieldsDynamic.size());
      SimpleField dynamicSubfield1 = subFieldsDynamic.get("subfield_1").getSimpleField();
      assertInstanceOf(String.class, dynamicSubfield1.getValue());
      assertEquals(FieldConfidence.High, dynamicSubfield1.getConfidence());
      for (Map.Entry<String, DynamicField> entry : subFieldsDynamic.entrySet()) {
        String fieldName = entry.getKey();
        SimpleField subField = entry.getValue().getSimpleField();
      }

      LinkedHashMap<String, SimpleField> subFieldsSimple = fieldObject.getSimpleFields();
      assertEquals(2, subFieldsSimple.size());
      SimpleField simpleSubfield1 = subFieldsSimple.get("subfield_1");
      assertEquals(FieldConfidence.High, simpleSubfield1.getConfidence());
      for (Map.Entry<String, SimpleField> entry : subFieldsSimple.entrySet()) {
        String fieldName = entry.getKey();
        SimpleField subField = entry.getValue();
      }
    }
  }

  @Test
  @DisplayName("allow getting fields using generics")
  void standardFieldTypes_getWithGenerics() throws IOException {
    InferenceResponse response = loadFromResource("v2/inference/standard_field_types.json");
    Inference inference = response.getInference();
    assertNotNull(inference);
    InferenceFields fields = inference.getResult().getFields();

    assertEquals(
        fields.get("field_simple_bool").getSimpleField(),
        fields.get("field_simple_bool").getField(SimpleField.class)
    );
    assertEquals(
        fields.get("field_simple_bool").getSimpleField(),
        fields.getSimpleField("field_simple_bool")
    );

    assertEquals(
        fields.get("field_simple_list").getListField(),
        fields.get("field_simple_list").getField(ListField.class)
    );
    assertEquals(
        fields.get("field_simple_list").getListField(),
        fields.getListField("field_simple_list")
    );

    assertEquals(
        fields.get("field_object").getObjectField(),
        fields.get("field_object").getField(ObjectField.class)
    );
    assertEquals(
        fields.get("field_object").getObjectField(),
        fields.getObjectField("field_object")
    );
  }

  @Test
  @DisplayName("confidence and locations must be usable")
  void standardFieldTypes_confidenceAndLocations() throws IOException {
    InferenceResponse response = loadFromResource("v2/inference/standard_field_types.json");
    Inference inference = response.getInference();
    assertNotNull(inference);

    InferenceFields fields = inference.getResult().getFields();

    SimpleField fieldSimpleString = fields.get("field_simple_string").getField(SimpleField.class);
    FieldConfidence confidence = fieldSimpleString.getConfidence();
    boolean isCertain = confidence == FieldConfidence.Certain;
    assertTrue(isCertain);

    List<FieldLocation> locations = fieldSimpleString.getLocations();
    assertEquals(1, locations.size());
    FieldLocation location = locations.get(0);

    Polygon polygon = location.getPolygon();
    List<Point> coords = polygon.getCoordinates();
    assertEquals(4, coords.size());
    double topX = coords.get(0).getX();
    assertEquals(0.0, topX);

    Point center = polygon.getCentroid();
    assertEquals(0.5, center.getX(), 0.00001);

    int pageIndex = location.getPage();
    assertEquals(0, pageIndex);
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
  @DisplayName("rst display")
  class RstDisplay {
    @Test
    @DisplayName("rst display must be parsed and exposed")
    void rstDisplay_mustBeAccessible() throws IOException {
      InferenceResponse resp = loadFromResource("v2/inference/standard_field_types.json");
      String rstRef = readFileAsString("v2/inference/standard_field_types.rst");
      Inference inf = resp.getInference();
      assertNotNull(inf);
      assertEquals(rstRef, resp.getInference().toString());
    }
  }
}
