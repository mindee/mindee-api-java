package com.mindee.parsing.v2;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static com.mindee.TestingUtilities.readFileAsString;
import static org.junit.jupiter.api.Assertions.*;

import com.mindee.geometry.Point;
import com.mindee.geometry.Polygon;
import com.mindee.input.LocalResponse;
import com.mindee.parsing.v2.field.DynamicField;
import com.mindee.parsing.v2.field.DynamicField.FieldType;
import com.mindee.parsing.v2.field.FieldConfidence;
import com.mindee.parsing.v2.field.FieldLocation;
import com.mindee.parsing.v2.field.InferenceFields;
import com.mindee.parsing.v2.field.ListField;
import com.mindee.parsing.v2.field.ObjectField;
import com.mindee.parsing.v2.field.SimpleField;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Inference Tests")
class InferenceTest {

  private InferenceResponse loadInference(String filePath) throws IOException {
    LocalResponse localResponse = new LocalResponse(getV2ResourcePath(filePath));
    return localResponse.deserializeResponse(InferenceResponse.class);
  }

  @Nested
  @DisplayName("Inference on blank file")
  class BlankPredictionTest {

    @Test
    @DisplayName("all properties must be valid")
    void asyncPredict_whenEmpty_mustHaveValidProperties() throws IOException {
      InferenceResponse response = loadInference("products/financial_document/blank.json");
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
  @DisplayName("Inference on filled file")
  class CompletePredictionTest {

    @Test
    @DisplayName("every exposed property must be valid and consistent")
    void asyncPredict_whenComplete_mustExposeAllProperties() throws IOException {
      InferenceResponse response = loadInference("products/financial_document/complete.json");
      Inference inference = response.getInference();
      assertNotNull(inference);
      assertEquals("12345678-1234-1234-1234-123456789abc", inference.getId());

      InferenceModel model = inference.getModel();
      assertNotNull(model, "Model must not be null");
      assertEquals("12345678-1234-1234-1234-123456789abc", model.getId());

      InferenceFile file = inference.getFile();
      assertNotNull(file);
      assertEquals("complete.jpg", file.getName());
      assertEquals(1, file.getPageCount());
      assertEquals("image/jpeg", file.getMimeType());
      assertNull(file.getAlias());

      InferenceFields fields = inference.getResult().getFields();
      assertEquals(21, fields.size());

      SimpleField date = fields.get("date").getSimpleField();
      assertEquals("2019-11-02", date.getValue());

      // list of objects
      DynamicField taxes = fields.get("taxes");
      assertNotNull(taxes);
      ListField taxesList = taxes.getListField();
      assertNotNull(taxesList);
      assertEquals(1, taxesList.getItems().size());
      ObjectField taxItemObj = taxesList.getItems().get(0).getObjectField();
      assertNotNull(taxItemObj);
      assertEquals(3, taxItemObj.getFields().size());
      SimpleField baseTax = taxItemObj.getFields().get("base").getSimpleField();
      assertEquals(31.5, baseTax.getValue());
      assertEquals(31.5, baseTax.getDoubleValue());
      assertEquals(BigDecimal.valueOf(31.5), baseTax.getBigDecimalValue());
      assertNotNull(taxes.toString());

      // single object
      DynamicField supplierAddress = fields.get("supplier_address");
      assertNotNull(supplierAddress);
      ObjectField supplierObj = supplierAddress.getObjectField();
      assertEquals(9, supplierObj.getFields().size());
      assertNotNull(supplierObj);
      DynamicField country = supplierObj.getFields().get("country");
      assertNotNull(country);
      assertEquals("USA", country.getSimpleField().getValue());
      assertEquals("USA", country.toString());
      assertNotNull(supplierAddress.toString());

      ObjectField customerAddr = fields.get("customer_address").getObjectField();
      SimpleField city = customerAddr.getFields().get("city").getSimpleField();
      assertEquals("New York", city.getValue(), "City mismatch");

      InferenceActiveOptions activeOptions = inference.getActiveOptions();
      assertNotNull(activeOptions);
      assertFalse(activeOptions.getConfidence());
      assertFalse(activeOptions.getRag());
      assertFalse(activeOptions.getRawText());
      assertFalse(activeOptions.getTextContext());
      assertFalse(activeOptions.getPolygon());
    }
  }

  @Nested
  @DisplayName("deep_nested_fields.json")
  class DeepNestedFieldsTest {

    @Test
    @DisplayName("all nested structures must be typed correctly")
    void deepNestedFields_mustExposeCorrectTypes() throws IOException {
      InferenceResponse resp = loadInference("inference/deep_nested_fields.json");
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
      SimpleField deepSimple = firstItem
        .getFields()
        .get("sub_object_object_sub_object_list_simple")
        .getSimpleField();
      assertEquals("value_9", deepSimple.getValue());
    }
  }

  @Nested
  @DisplayName("standard_field_types.json")
  class StandardFieldTypesTest {

    private void testSimpleFieldString(SimpleField field) {
      assertNotNull(field);
      assertEquals(field.getValue(), field.getStringValue());
      assertThrows(ClassCastException.class, field::getDoubleValue);
      assertThrows(ClassCastException.class, field::getBooleanValue);
      assertInstanceOf(List.class, field.getLocations());
    }

    @Test
    @DisplayName("simple fields must be recognised")
    void standardFieldTypes_mustExposeSimpleFieldValues() throws IOException {
      InferenceResponse response = loadInference("inference/standard_field_types.json");
      Inference inference = response.getInference();
      assertNotNull(inference);

      InferenceFields fields = inference.getResult().getFields();

      assertNotNull(fields.get("field_simple_string").getSimpleField());

      SimpleField fieldSimpleString = fields.getSimpleField("field_simple_string");
      testSimpleFieldString(fieldSimpleString);
      assertEquals(FieldConfidence.Certain, fieldSimpleString.getConfidence());
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
      assertThrows(ClassCastException.class, fieldSimpleInt::getStringValue);

      SimpleField fieldSimpleZero = fields.get("field_simple_zero").getSimpleField();
      assertNotNull(fieldSimpleZero);
      assertEquals(FieldConfidence.Low, fieldSimpleZero.getConfidence());
      assertInstanceOf(Double.class, fieldSimpleZero.getValue());
      assertEquals(0.0, fieldSimpleZero.getDoubleValue());
      assertEquals(BigDecimal.valueOf(0.0), fieldSimpleZero.getBigDecimalValue());
      assertThrows(ClassCastException.class, fieldSimpleZero::getStringValue);
      assertThrows(ClassCastException.class, fieldSimpleZero::getBooleanValue);

      SimpleField fieldSimpleBool = fields.get("field_simple_bool").getSimpleField();
      assertNotNull(fieldSimpleBool);
      assertInstanceOf(Boolean.class, fieldSimpleBool.getValue());
      assertEquals(fieldSimpleBool.getValue(), fieldSimpleBool.getBooleanValue());
      assertThrows(ClassCastException.class, fieldSimpleBool::getStringValue);
      assertThrows(ClassCastException.class, fieldSimpleBool::getDoubleValue);
      assertThrows(ClassCastException.class, fieldSimpleBool::getBigDecimalValue);

      SimpleField fieldSimpleNull = fields.get("field_simple_null").getSimpleField();
      assertNotNull(fieldSimpleNull);
      assertNull(fieldSimpleNull.getValue());
      assertNull(fieldSimpleNull.getStringValue());
      assertNull(fieldSimpleNull.getBooleanValue());
      assertNull(fieldSimpleNull.getDoubleValue());
      assertNull(fieldSimpleNull.getBigDecimalValue());
    }

    @Test
    @DisplayName("simple list fields must be recognised")
    void standardFieldTypes_mustExposeSimpleListFieldValues() throws IOException {
      InferenceResponse response = loadInference("inference/standard_field_types.json");
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
        testSimpleFieldString(itemField);
        assertEquals(1, itemField.getLocations().size());
      }

      // High level (typed) access
      List<SimpleField> simpleItems = listField.getSimpleItems();
      assertEquals(2, simpleItems.size());
      SimpleField firstSimpleItem = simpleItems.get(0);
      assertEquals(FieldConfidence.Medium, firstSimpleItem.getConfidence());
      for (SimpleField itemField : simpleItems) {
        testSimpleFieldString(itemField);
        assertEquals(1, itemField.getLocations().size());
      }

      assertThrows(IllegalStateException.class, listField::getObjectItems);
    }

    private void testObjectSubFieldSimpleString(String fieldName, SimpleField subField) {
      assertTrue(fieldName.startsWith("subfield_"));
      testSimpleFieldString(subField);
    }

    @Test
    @DisplayName("object list fields must be recognised")
    void standardFieldTypes_mustExposeObjectListFieldValues() throws IOException {
      InferenceResponse response = loadInference("inference/standard_field_types.json");
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
          String fieldName = entry.getKey();
          SimpleField subfield = entry.getValue();
          testObjectSubFieldSimpleString(fieldName, subfield);
        }
      }

      assertThrows(IllegalStateException.class, listField::getSimpleItems);
    }

    @Test
    @DisplayName("simple / object / list variants must be recognised")
    void standardFieldTypes_mustExposeObjectFieldValues() throws IOException {
      InferenceResponse response = loadInference("inference/standard_field_types.json");
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
        testObjectSubFieldSimpleString(fieldName, subField);
      }

      LinkedHashMap<String, SimpleField> subFieldsSimple = fieldObject.getSimpleFields();
      assertEquals(2, subFieldsSimple.size());
      SimpleField simpleSubfield1 = subFieldsSimple.get("subfield_1");
      assertEquals(FieldConfidence.High, simpleSubfield1.getConfidence());
      for (Map.Entry<String, SimpleField> entry : subFieldsSimple.entrySet()) {
        String fieldName = entry.getKey();
        SimpleField subField = entry.getValue();
        testObjectSubFieldSimpleString(fieldName, subField);
      }
    }
  }

  @Test
  @DisplayName("allow getting fields using generics")
  void standardFieldTypes_getWithGenerics() throws IOException {
    InferenceResponse response = loadInference("inference/standard_field_types.json");
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
    InferenceResponse response = loadInference("inference/standard_field_types.json");
    Inference inference = response.getInference();
    assertNotNull(inference);

    InferenceFields fields = inference.getResult().getFields();

    SimpleField fieldSimpleString = fields.get("field_simple_string").getField(SimpleField.class);
    FieldConfidence confidence = fieldSimpleString.getConfidence();
    boolean isCertain = confidence == FieldConfidence.Certain;
    assertTrue(isCertain);
    assertEquals(3, confidence.ordinal());
    assertTrue(confidence.greaterThanOrEqual(FieldConfidence.Certain));
    assertTrue(confidence.greaterThan(FieldConfidence.Medium));
    assertTrue(confidence.lessThanOrEqual(FieldConfidence.Certain));
    assertFalse(confidence.lessThan(FieldConfidence.Certain));

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
  @DisplayName("Raw Text")
  class RawTextTest {

    @Test
    @DisplayName("raw texts option must be parsed and exposed")
    void rawTexts_mustBeAccessible() throws IOException {
      InferenceResponse response = loadInference("inference/raw_texts.json");
      Inference inference = response.getInference();
      assertNotNull(inference);

      InferenceActiveOptions activeOptions = inference.getActiveOptions();
      assertNotNull(activeOptions);
      assertFalse(activeOptions.getRag());
      assertTrue(activeOptions.getRawText());
      assertFalse(activeOptions.getPolygon());
      assertFalse(activeOptions.getConfidence());
      assertFalse(activeOptions.getTextContext());
      assertFalse(activeOptions.getDataSchema().getReplace());

      assertNull(inference.getResult().getRag());

      RawText rawText = inference.getResult().getRawText();
      assertEquals(2, rawText.getPages().size());

      for (RawTextPage page : rawText.getPages()) {
        assertNotNull(page.getContent());
        assertFalse(page.getContent().isEmpty());
      }

      RawTextPage first = rawText.getPages().get(0);
      assertEquals("This is the raw text of the first page...", first.getContent());
    }
  }

  @Nested
  @DisplayName("Rag Metadata")
  class RagMetadataTest {

    @Test
    @DisplayName("RAG metadata when matched")
    void rag_mustBeFilled_whenMatched() throws IOException {
      InferenceResponse response = loadInference("inference/rag_matched.json");
      Inference inference = response.getInference();
      assertNotNull(inference);

      RagMetadata rag = inference.getResult().getRag();
      assertNotNull(rag);
      assertEquals("12345abc-1234-1234-1234-123456789abc", rag.getRetrievedDocumentId());
    }

    @Test
    @DisplayName("RAG metadata when not matched")
    void rag_mustBeNull_whenNotMatched() throws IOException {
      InferenceResponse response = loadInference("inference/rag_not_matched.json");
      Inference inference = response.getInference();
      assertNotNull(inference);

      RagMetadata rag = inference.getResult().getRag();
      assertNotNull(rag);
      assertNull(rag.getRetrievedDocumentId());
    }
  }

  @Nested
  @DisplayName("RST Display")
  class RstDisplay {
    @Test
    @DisplayName("rst display must be parsed and exposed")
    void rstDisplay_mustBeAccessible() throws IOException {
      InferenceResponse resp = loadInference("inference/standard_field_types.json");
      String rstRef = readFileAsString(getV2ResourcePath("inference/standard_field_types.rst"));
      Inference inference = resp.getInference();
      assertNotNull(inference);
      assertEquals(rstRef, resp.getInference().toString());
    }
  }

  @Nested
  @DisplayName("Text Context Return")
  class TextContextTest {
    @Test
    @DisplayName("should be present and true when enabled")
    void textContext_mustBePresentAndTrue() throws IOException {
      InferenceResponse resp = loadInference("inference/text_context_enabled.json");
      Inference inference = resp.getInference();
      assertNotNull(inference);
      assertTrue(inference.getActiveOptions().getTextContext());
    }
  }

  @Nested
  @DisplayName("Data Schema Return")
  class DataSchemaTest {
    @Test
    @DisplayName("should be present and true when enabled")
    void textContext_mustBePresentAndTrue() throws IOException {
      InferenceResponse resp = loadInference("inference/data_schema_replace.json");
      Inference inference = resp.getInference();
      assertNotNull(inference);
      InferenceFields fields = inference.getResult().getFields();
      assertEquals("a test value", fields.get("test_replace").getSimpleField().getStringValue());

      assertTrue(inference.getActiveOptions().getDataSchema().getReplace());
    }
  }
}
