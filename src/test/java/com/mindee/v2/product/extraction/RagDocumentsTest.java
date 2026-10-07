package com.mindee.v2.product.extraction;

import static com.mindee.TestingUtilities.getV2ProductPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.v2.parsing.LocalResponse;
import com.mindee.v2.product.extraction.ragdocuments.AnnotatedDynamicField;
import com.mindee.v2.product.extraction.ragdocuments.AnnotatedFields;
import com.mindee.v2.product.extraction.ragdocuments.AnnotatedListField;
import com.mindee.v2.product.extraction.ragdocuments.AnnotatedObjectField;
import com.mindee.v2.product.extraction.ragdocuments.AnnotatedSimpleField;
import com.mindee.v2.product.extraction.ragdocuments.ExtractionRagAnnotationResponse;
import com.mindee.v2.product.extraction.ragdocuments.RagAnnotation;
import com.mindee.v2.product.extraction.ragdocuments.params.RagDocumentAnnotationParameters;
import com.mindee.v2.product.extraction.ragdocuments.params.RagDocumentUploadParameters;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Extraction Rag Documents")
public class RagDocumentsTest {

  static ObjectMapper mapper = new ObjectMapper();
  private static String expectedAnnotation;

  @BeforeAll
  static void init() throws IOException {
    String rawJson = Files
      .readString(getV2ProductPath("extraction/rag_documents/test_annotation.json"));
    expectedAnnotation = mapper.readTree(rawJson).toString();
  }

  @Test
  @DisplayName("should init POST parameters")
  void postParameters_mustInit() {
    RagDocumentUploadParameters parameters = RagDocumentUploadParameters
      .builder("invalid-model-id")
      .build();

    Map<String, String> reqParams = parameters.getRequestParameters();
    assertEquals("invalid-model-id", reqParams.get("model_id"));
  }

  @Test
  @DisplayName("Should init PATCH parameters from an annotation instance.")
  void patchParameters_mustInitFromObject() throws JsonProcessingException {
    var fields = new AnnotatedFields();
    fields.put("simple", new AnnotatedDynamicField(new AnnotatedSimpleField(true, false, null)));
    fields
      .put(
        "list",
        new AnnotatedDynamicField(new AnnotatedListField(new ArrayList<>(), false, null))
      );
    fields
      .put(
        "object",
        new AnnotatedDynamicField(new AnnotatedObjectField(new AnnotatedFields(), false, null))
      );

    var annotation = new RagAnnotation(fields);
    RagDocumentAnnotationParameters parameters = RagDocumentAnnotationParameters
      .builder("invalid-document-id")
      .status("Active")
      .annotation(annotation)
      .build();

    var reqParams = parameters.getRequestParameters();
    assertEquals("invalid-document-id", parameters.getDocumentId());
    assertEquals("Active", reqParams.get("status"));
    assertEquals(expectedAnnotation, mapper.writeValueAsString(reqParams.get("annotation")));
  }

  @Test
  @DisplayName("should load a POST response from a JSON string")
  void ragDocumentsPost_mustHaveValidProperties() throws IOException {
    var response = loadResponse("extraction/rag_documents/post_response.json");

    assertNotNull(response);
    assertEquals("cc831599-c545-48b7-aa27-6d7ccd5b8d32", response.getId());
    assertEquals("Processing", response.getStatus());
    assertNull(response.getAnnotation());
  }

  @Test
  @DisplayName("should load a GET response from a JSON string")
  void ragDocumentsGetDraft_mustHaveValidProperties() throws IOException {
    var response = loadResponse("extraction/rag_documents/get_response_draft.json");

    assertNotNull(response);
    assertEquals("cc831599-c545-48b7-aa27-6d7ccd5b8d32", response.getId());
    assertEquals("Draft", response.getStatus());
    assertNotNull(response.getAnnotation());

    var fields = response.getAnnotation().getFields();
    assertNotNull(fields);

    // null simple field
    var tipField = fields.getSimpleField("tip");
    assertNotNull(tipField);
    assertFalse(tipField.isSelected());
    assertNull(tipField.getGuidelines());
    assertNull(tipField.getValue());

    // filled simple field
    var dateField = fields.getSimpleField("date");
    assertNotNull(dateField);
    assertFalse(dateField.isSelected());
    assertNull(dateField.getGuidelines());
    assertEquals("2019-11-02", dateField.getValue());

    // filled object field
    var localeField = fields.getObjectField("locale");
    assertNotNull(localeField);
    assertFalse(localeField.isSelected());
    assertNull(localeField.getGuidelines());
    assertNotNull(localeField.getFields());
    assertEquals(3, localeField.getFields().size());
    assertEquals("US", localeField.getFields().getSimpleField("country").getValue());
    assertEquals("USD", localeField.getFields().getSimpleField("currency").getValue());
    assertNull(localeField.getFields().getSimpleField("language").getValue());

    // list of simple fields
    var referenceNumbersField = fields.getListField("reference_numbers");
    assertNotNull(referenceNumbersField);
    assertFalse(referenceNumbersField.isSelected());
    assertNull(referenceNumbersField.getGuidelines());
    assertNotNull(referenceNumbersField.getSimpleItems());
    assertEquals(1, referenceNumbersField.getSimpleItems().size());
    assertEquals("2412/2019", referenceNumbersField.getSimpleItems().get(0).getValue());

    // list of object fields
    var lineItemsField = fields.getListField("line_items");
    assertNotNull(lineItemsField);
    assertFalse(lineItemsField.isSelected());
    assertNull(lineItemsField.getGuidelines());
    assertNotNull(lineItemsField.getObjectItems());
    assertEquals(3, lineItemsField.getObjectItems().size());

    var lineItem0 = lineItemsField.getObjectItems().get(0);
    assertNotNull(lineItem0.getFields());
    assertEquals(8, lineItem0.getFields().size());
    assertEquals(
      "Front and rear brake cables",
      lineItem0.getFields().getSimpleField("description").getValue()
    );
    assertEquals(1.0, lineItem0.getFields().getSimpleField("quantity").getValue());
    assertEquals(100.0, lineItem0.getFields().getSimpleField("unit_price").getValue());
    assertEquals(100.0, lineItem0.getFields().getSimpleField("total_price").getValue());
    assertNull(lineItem0.getFields().getSimpleField("tax_rate").getValue());
    assertNull(lineItem0.getFields().getSimpleField("tax_amount").getValue());
    assertNull(lineItem0.getFields().getSimpleField("product_code").getValue());
    assertNull(lineItem0.getFields().getSimpleField("unit_measure").getValue());

    var lineItem1 = lineItemsField.getObjectItems().get(1);
    assertNotNull(lineItem1.getFields());
    assertEquals(8, lineItem1.getFields().size());
    assertEquals(
      "New set of pedal arms",
      lineItem1.getFields().getSimpleField("description").getValue()
    );
    assertEquals(2.0, lineItem1.getFields().getSimpleField("quantity").getValue());
    assertEquals(25.0, lineItem1.getFields().getSimpleField("unit_price").getValue());
    assertEquals(50.0, lineItem1.getFields().getSimpleField("total_price").getValue());
    assertNull(lineItem1.getFields().getSimpleField("tax_rate").getValue());
    assertNull(lineItem1.getFields().getSimpleField("tax_amount").getValue());
    assertNull(lineItem1.getFields().getSimpleField("product_code").getValue());
    assertNull(lineItem1.getFields().getSimpleField("unit_measure").getValue());

    var lineItem2 = lineItemsField.getObjectItems().get(2);
    assertNotNull(lineItem2.getFields());
    assertEquals(8, lineItem2.getFields().size());
    assertEquals("Labor 3hrs", lineItem2.getFields().getSimpleField("description").getValue());
    assertEquals(3.0, lineItem2.getFields().getSimpleField("quantity").getValue());
    assertEquals(15.0, lineItem2.getFields().getSimpleField("unit_price").getValue());
    assertEquals(45.0, lineItem2.getFields().getSimpleField("total_price").getValue());
    assertNull(lineItem2.getFields().getSimpleField("tax_rate").getValue());
    assertNull(lineItem2.getFields().getSimpleField("tax_amount").getValue());
    assertNull(lineItem2.getFields().getSimpleField("product_code").getValue());
    assertNull(lineItem2.getFields().getSimpleField("unit_measure").getValue());
  }

  private ExtractionRagAnnotationResponse loadResponse(String filePath) throws IOException {
    var localResponse = new LocalResponse(getV2ProductPath(filePath));
    return localResponse.deserializeResponse(ExtractionRagAnnotationResponse.class);
  }
}
