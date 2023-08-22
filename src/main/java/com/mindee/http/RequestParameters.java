package com.mindee.http;

import com.mindee.PredictOptions;
import java.net.URL;
import lombok.Builder;
import lombok.Value;

/**
 * Parameters for making API calls to the endpoint.
 */
@Value
public class RequestParameters {

  URL fileUrl;
  byte[] file;
  String fileName;
  PredictOptions predictOptions;

  @Builder
  private RequestParameters(
      URL urlInputSource,
      byte[] file,
      PredictOptions predictOptions,
      String fileName
  ) {
    if (file != null && urlInputSource != null) {
      throw new IllegalArgumentException("Only one of urlInputSource or file bytes are allowed");
    }
    if (predictOptions == null) {
      this.predictOptions = PredictOptions.builder().build();
    } else {
      this.predictOptions = predictOptions;
    }
    this.fileUrl = urlInputSource;
    this.file = file;
    this.fileName = fileName;
  }
}
