package com.mindee.documentparser;

import com.mindee.http.Endpoint;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class EndpointUtilsTest {

  @Test
  void givenAnEndpoint_whenUrlBuilt_returnsCorrectUrl() {
    final String expectedUrl = "https://api.mindee.net/v1/products/someowner/randomname/v3.2/predict";
    Endpoint endpoint = Endpoint.builder()
        .keyName("something")
        .urlName("randomname")
        .version("3.2")
        .owner("someowner")
        .apiKey("123243")
        .build();
    String url = EndpointUtils.buildUrl(endpoint);
    Assert.assertNotNull(url);
    Assert.assertEquals(expectedUrl, url);
  }
}