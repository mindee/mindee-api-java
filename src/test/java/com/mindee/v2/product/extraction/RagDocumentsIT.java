package com.mindee.v2.product.extraction;

import static com.mindee.TestingUtilities.getV2ProductPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.http.MindeeHttpExceptionV2;
import com.mindee.v2.product.extraction.ragdocuments.ExtractionRagAnnotationResponse;
import com.mindee.v2.product.extraction.ragdocuments.params.RagDocumentAnnotationParameters;
import com.mindee.v2.product.extraction.ragdocuments.params.RagDocumentUploadParameters;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@DisplayName("MindeeV2 – Integration")
public class RagDocumentsIT {
  private MindeeClient client;
  private String modelId;

  @BeforeAll
  void setUp() {
    String apiKey = System.getenv("MINDEE_V2_API_KEY");
    modelId = System.getenv("MINDEE_V2_SE_TESTS_FINDOC_MODEL_ID");
    client = new MindeeClient(apiKey);
  }

  @Test
  @DisplayName("Should perform the entire lifecycle of a RAG document.")
  void ragDocument_lifecycle_mustSucceed() throws IOException, InterruptedException {
    var inputSource = new LocalInputSource(
      getV2ProductPath("extraction/financial_document/default_sample.jpg")
    );
    var parameters = RagDocumentUploadParameters.builder(modelId).build();

    var postResponse = client.uploadAndGetRagDocument(inputSource, parameters);
    assertNotNull(postResponse);

    var postAnnotation = postResponse.getAnnotation();
    assertNotNull(postAnnotation.getFields());

    var documentId = postResponse.getId();
    assertNotNull(documentId);

    assertEquals("Draft", postResponse.getStatus());

    postAnnotation.getFields().get("supplier_name").getSimpleField().setSelected(true);
    postAnnotation
      .getFields()
      .get("supplier_name")
      .getSimpleField()
      .setGuidelines("I am the walrus!");
    postAnnotation.getFields().get("invoice_number").getSimpleField().setSelected(true);
    postAnnotation
      .getFields()
      .get("invoice_number")
      .getSimpleField()
      .setGuidelines("koo koo katchoo!");

    var patchAnnotationParameters = RagDocumentAnnotationParameters
      .builder(documentId)
      .annotation(postAnnotation)
      .build();

    var patchAnnotationResponse = client.updateRagAnnotation(patchAnnotationParameters);
    assertNotNull(patchAnnotationResponse);
    var patchAnnotation = patchAnnotationResponse.getAnnotation();
    assertEquals(
      "I am the walrus!",
      patchAnnotation.getFields().get("supplier_name").getSimpleField().getGuidelines()
    );
    assertTrue(patchAnnotation.getFields().get("supplier_name").getSimpleField().isSelected());
    assertEquals(
      "koo koo katchoo!",
      patchAnnotation.getFields().get("invoice_number").getSimpleField().getGuidelines()
    );
    assertTrue(patchAnnotation.getFields().get("invoice_number").getSimpleField().isSelected());

    var getResponse = client.getReadyRagDocument(ExtractionRagAnnotationResponse.class, documentId);
    assertNotNull(getResponse);
    var getAnnotation = getResponse.getAnnotation();
    assertNotNull(getAnnotation);

    assertEquals("Draft", getResponse.getStatus());

    assertEquals(
      "I am the walrus!",
      getAnnotation.getFields().get("supplier_name").getSimpleField().getGuidelines()
    );
    assertTrue(getAnnotation.getFields().get("supplier_name").getSimpleField().isSelected());
    assertEquals(
      "koo koo katchoo!",
      getAnnotation.getFields().get("invoice_number").getSimpleField().getGuidelines()
    );
    assertTrue(getAnnotation.getFields().get("invoice_number").getSimpleField().isSelected());

    var patchStatusParameters = RagDocumentAnnotationParameters
      .builder(documentId)
      .status("Active")
      .build();

    var patchStatusResponse = client.updateAndGetRagAnnotation(patchStatusParameters);
    assertNotNull(patchStatusResponse);
    assertEquals("Active", patchStatusResponse.getStatus());

    boolean deleteResponse = client.deleteExtractionRagDocument(documentId);
    assertTrue(deleteResponse);

    assertThrows(MindeeHttpExceptionV2.class, () -> {
      client.getRagDocument(ExtractionRagAnnotationResponse.class, documentId);
    });
  }
}
