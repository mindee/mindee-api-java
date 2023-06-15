package com.mindee.http;

import com.mindee.MindeeException;
import com.mindee.parsing.common.Inference;
import lombok.Getter;

/**
 * Endpoint for custom documents.
 */
@Getter
public final class Endpoint {

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

  public Endpoint(String endpointName, String accountName) {
    this.endpointName = endpointName;
    this.accountName = accountName;
  }

  public Endpoint(String endpointName, String accountName, String version) {
    this.endpointName = endpointName;
    this.accountName = accountName;
    this.version = version;
  }

  public <DocT extends Inference> Endpoint(Class<DocT> documentClass) {
    EndpointInfo endpointAnnotation = documentClass.getAnnotation(EndpointInfo.class);

    // that means it could be custom document
    if (endpointAnnotation == null) {
      CustomEndpointInfo customEndpointAnnotation = documentClass.getAnnotation(
          CustomEndpointInfo.class
      );
      if (customEndpointAnnotation == null) {
        throw new MindeeException(
          "The class is not supported as a prediction model. "
            + "The endpoint attribute is missing. "
            + "Please refer to the document or contact support."
        );
      }
      this.endpointName = customEndpointAnnotation.endpointName();
      this.accountName = customEndpointAnnotation.accountName();
      this.version = customEndpointAnnotation.version();
      return;
    }
    this.endpointName = endpointAnnotation.endpointName();
    this.accountName = endpointAnnotation.accountName();
    this.version = endpointAnnotation.version();
  }
}
