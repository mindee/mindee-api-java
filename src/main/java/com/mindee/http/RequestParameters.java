package com.mindee.http;

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
  Boolean allWords;
  String fileName;
  Boolean asyncCall;

  @Builder
  private RequestParameters(
      URL urlInputSource,
      byte[] file,
      Boolean allWords,
      String fileName,
      Boolean asyncCall
  ) {
    if (file != null && urlInputSource != null) {
      throw new IllegalArgumentException("Only one of urlInputSource or file bytes are allowed");
    }
    if (allWords == null) {
      this.allWords = Boolean.FALSE;
    } else {
      this.allWords = allWords;
    }
    this.fileUrl = urlInputSource;
    this.file = file;

    this.fileName = fileName;
    if (asyncCall == null) {
      this.asyncCall = Boolean.FALSE;
    } else {
      this.asyncCall = asyncCall;
    }
  }
}
