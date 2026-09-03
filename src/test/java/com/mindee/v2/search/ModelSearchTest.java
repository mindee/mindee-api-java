package com.mindee.v2.search;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindee.v2.parsing.LocalResponse;
import com.mindee.v2.search.models.ModelSearchResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class ModelSearchTest {
  @Test
  public void modelSearchResponse_LoadsLocally() throws IOException {
    LocalResponse localResponse = new LocalResponse(getV2ResourcePath("search/models.json"));
    ModelSearchResponse response = localResponse.deserializeResponse(ModelSearchResponse.class);

    assertNotNull(response);

    assertEquals(5, response.getModels().size());
    assertEquals(5, response.getPagination().getTotalItems());
    assertEquals(1, response.getPagination().getPage());
    assertEquals(50, response.getPagination().getPerPage());
    assertEquals(1, response.getPagination().getTotalPages());

    var firstItem = response.getModels().get(0);
    assertEquals("Extraction With Webhooks", firstItem.getName());
    assertEquals("afde5151-aa11-aa11-9289-fa04e50ca3b9", firstItem.getId());
    assertEquals("extraction", firstItem.getModelType());

    assertEquals(2, firstItem.getWebhooks().size());
    assertEquals("a2286ed9-aa11-aa11-bdc5-2f8496c5641a", firstItem.getWebhooks().get(0).getId());
    assertEquals("FAILURE", firstItem.getWebhooks().get(0).getName());
    assertEquals("https://failure.mindee.com", firstItem.getWebhooks().get(0).getUrl());

    var lastItem = response.getModels().get(response.getModels().size() - 1);
    assertEquals("Extraction Without Webhooks Key", lastItem.getName());
    assertEquals("e14e0923-ee55-ee55-a335-8d2110917d7b", lastItem.getId());
  }
}
