package com.mindee.v2.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindee.v2.MindeeClient;
import com.mindee.v2.search.models.ModelSearchParameters;
import com.mindee.v2.search.models.ModelSearchResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
public class ModelSearchIT {

  private MindeeClient client;

  @BeforeAll
  void setUp() {
    var apiKey = System.getenv("MINDEE_V2_API_KEY");
    client = new MindeeClient(apiKey);
  }

  @Test
  public void ModelSearch_mustHaveResults() throws Exception {
    ModelSearchResponse response = client
      .search(ModelSearchResponse.class, ModelSearchParameters.builder().build());

    assertNotNull(response);
    assertNotNull(response.getModels());
    assertFalse(response.getModels().isEmpty());
    assertNotNull(response.getPagination());
    assertTrue(response.getPagination().getTotalItems() > 1);
    assertEquals(1, response.getPagination().getPage());
  }

  @Test
  public void ModelSearch_mustReturnEmpty() throws Exception {
    ModelSearchResponse response = client
      .search(
        ModelSearchResponse.class,
        ModelSearchParameters.builder().name("je n'existe pas tralala").build()
      );

    assertNotNull(response);
    assertNotNull(response.getModels());
    assertTrue(response.getModels().isEmpty());
    assertNotNull(response.getPagination());
    assertEquals(0, response.getPagination().getTotalItems());
    assertEquals(1, response.getPagination().getPage());
  }

  @Test
  @SuppressWarnings("deprecation")
  public void ModelSearch_mustReturnEmptyObsolete() throws Exception {
    ModelSearchResponse response = client.searchModels("je n'existe pas tralala");

    assertNotNull(response);
    assertNotNull(response.getModels());
    assertTrue(response.getModels().isEmpty());
    assertNotNull(response.getPagination());
    assertEquals(0, response.getPagination().getTotalItems());
    assertEquals(1, response.getPagination().getPage());
  }
}
