package com.mindee.documentparser;

import com.mindee.http.Endpoint;

final class EndpointUtils {

  private static final String MINDEE_API_URL = "https://api.mindee.net/v1";

  private EndpointUtils() {
  }

  static String buildUrl(Endpoint endpoint) {
    StringBuilder builder = new StringBuilder(MINDEE_API_URL);
    builder.append("/products/");
    builder.append(endpoint.getOwner());
    builder.append("/");
    builder.append(endpoint.getUrlName());
    builder.append("/v");
    builder.append(endpoint.getVersion());
    builder.append("/predict");
    return builder.toString();
  }
}
