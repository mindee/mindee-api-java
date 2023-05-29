package com.mindee.parsing;

import java.net.URL;
import lombok.Builder;
import lombok.Value;

/**
 * Parameters for making api calls to the endpoint
 */
@Value
public class RequestParameters {

  private URL fileUrl;
  private byte[] file;
  private Boolean includeWords;
  private String fileName;
  private Boolean asyncCall;

  @Builder
  private RequestParameters(URL fileUrl, byte[] file, Boolean includeWords,
      String fileName, Boolean asyncCall) {
    if (file != null && fileUrl != null) {
      throw new IllegalArgumentException("Only one of documentUrl or file bytes are allowed");
    }
    if (includeWords == null) {
      this.includeWords = Boolean.FALSE;
    } else {
      this.includeWords = includeWords;
    }
    this.fileUrl = fileUrl;
    this.file = file;

    this.fileName = fileName;
    if (asyncCall == null) {
      this.asyncCall = Boolean.FALSE;
    } else {
      this.asyncCall = asyncCall;
    }
  }
}
