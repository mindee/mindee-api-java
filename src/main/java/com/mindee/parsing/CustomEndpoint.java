package com.mindee.parsing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class CustomEndpoint {

  /**
   * The name of the product associated to the expected model.
   */
  private String endpointName;

  /**
   * The name of the account that owns the API. Useful when using custom builder.
   */
  private String accountName;

  /**
   * The version number of the API. Without the v (for example for the v1.2: 1.2).
   */
  private String version;
}
