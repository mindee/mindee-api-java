package com.mindee.parsing;

import lombok.Getter;

@Getter
public final class CustomEndpoint {

  /**
   * The name of the product associated to the expected model.
   */
  private final String endpointName;

  /**
   * The name of the account that owns the API. Useful when using custom builder.
   */
  private final String accountName;

  /**
   * The version number of the API. Without the v (for example for the v1.2: 1.2).
   */
  private String version = "1";

  public CustomEndpoint(String endpointName, String accountName) {
    this.endpointName = endpointName;
    this.accountName = accountName;
  }

  public CustomEndpoint(String endpointName, String accountName, String version) {
    this.endpointName = endpointName;
    this.accountName = accountName;
    this.version = version;
  }
}
