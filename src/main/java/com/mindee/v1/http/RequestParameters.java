package com.mindee.v1.http;

import com.mindee.input.URLInputSource;
import com.mindee.v1.clientoptions.PredictOptions;
import com.mindee.v1.clientoptions.WorkflowOptions;
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
  WorkflowOptions workflowOptions;

  @Builder
  private RequestParameters(
      URLInputSource urlInputSource,
      byte[] file,
      PredictOptions predictOptions,
      WorkflowOptions workflowOptions,
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
    if (workflowOptions == null) {
      this.workflowOptions = WorkflowOptions.builder().build();
    } else {
      this.workflowOptions = workflowOptions;
    }
    if (urlInputSource != null) {
      urlInputSource.validateSecure();
      this.fileUrl = urlInputSource.getUrl();
    } else {
      this.fileUrl = null;
    }
    this.file = file;
    this.fileName = fileName;
  }
}
