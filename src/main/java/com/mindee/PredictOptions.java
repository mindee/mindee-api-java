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
   * Whether to include barcode data for each page.
   * This performs a detection and extraction operation on the server and will increase response time.
   */

  @Builder
  private PredictOptions(
      Boolean allWords,
      Boolean cropper
  ) {
    this.allWords = allWords == null ? Boolean.FALSE : allWords;
    this.cropper = cropper == null ? Boolean.FALSE : cropper;
  }
}
