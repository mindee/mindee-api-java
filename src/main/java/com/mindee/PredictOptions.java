package com.mindee;

import lombok.Builder;
import lombok.Value;

/**
 * Parameters for making predict API calls.
 */
@Value
public class PredictOptions {
  /**
   * Whether to include the full text for each page.
   * This performs a full OCR operation on the server and will increase response time.
   */
  Boolean allWords;
  /**
   * Whether to include cropper results for each page.
   * This performs a cropping operation on the server and will increase response time.
   */
  Boolean cropper;
  /**
   * Whether to include the full text data for async APIs.
   * This performs a full OCR operation on the server and will increase response time & payload
   * size.
   */
  Boolean fullText;

  @Builder
  private PredictOptions(
      Boolean allWords,
      Boolean fullText,
      Boolean cropper
  ) {
    this.allWords = allWords == null ? Boolean.FALSE : allWords;
    this.fullText = fullText == null ? Boolean.FALSE : fullText;
    this.cropper = cropper == null ? Boolean.FALSE : cropper;
  }
}
