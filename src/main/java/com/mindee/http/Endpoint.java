package com.mindee.http;

import lombok.Builder;
import lombok.Value;

@Value
public class Endpoint {

  private final String owner;
  private final String urlName;
  private final String version;
  private final String keyName;
  private final String apiKey;

  @Builder
  private Endpoint(String owner, String urlName, String version, String keyName,
      String apiKey) {
    this.owner = owner;
    this.urlName = urlName;
    this.version = version;
    this.keyName = keyName;
    this.apiKey = apiKey;
  }
}
