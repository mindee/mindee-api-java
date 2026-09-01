package com.mindee.v2.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindee.v2.MindeeClient;
import com.mindee.v2.search.ragdocuments.RagDocumentSearchParameters;
import com.mindee.v2.search.ragdocuments.RagDocumentSearchResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
public class RagDocumentSearchIT {

  private MindeeClient client;
  private String findocModelId;

  @BeforeAll
  void setUp() {
    var apiKey = System.getenv("MINDEE_V2_API_KEY");
    client = new MindeeClient(apiKey);
    findocModelId = System.getenv("MINDEE_V2_SE_TESTS_FINDOC_MODEL_ID");
  }

  @Test
  public void RagDocumentSearch_mustHaveResults() throws Exception {
    RagDocumentSearchResponse response = client
      .search(
        RagDocumentSearchResponse.class,
        RagDocumentSearchParameters.builder(findocModelId).build()
      );

    assertNotNull(response);
    assertNotNull(response.getRagDocuments());
    assertNotNull(response.getPagination());
    assertEquals(1, response.getPagination().getPage());
  }
}
