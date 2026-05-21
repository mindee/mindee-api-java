package com.mindee.v2.parsing.search;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mindee.v2.parsing.LocalResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Search Models Tests")
public class SearchModelsTest {
  @Test
  void propertiesMustBePresent() throws IOException {
    var localResponse = new LocalResponse(getV2ResourcePath("search/models.json"));
    var searchResponse = localResponse.deserializeResponse(SearchResponse.class);

    assertEquals(5, searchResponse.getModels().size());

    var model0 = searchResponse.getModels().get(0);
    assertEquals("Extraction With Webhooks", model0.getName());
    assertEquals(2, model0.getWebhooks().size());

    var model0Webook0 = model0.getWebhooks().get(0);
    assertEquals("FAILURE", model0Webook0.getName());
    assertEquals("https://failure.mindee.com", model0Webook0.getUrl());
  }
}
