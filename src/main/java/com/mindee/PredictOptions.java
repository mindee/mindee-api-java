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
   * This performs a full OCR operation on the server and will increase response time and payload
   * size.
   */
  Boolean fullText;
  /**
   * If set, will enqueue to a workflow queue instead of a product's endpoint.
   */
  String workflowId;
  /**
   * If set, will enable Retrieval-Augmented Generation.
   * Only works if a valid workflowId is set.
   */
  Boolean rag;

  @Builder
  private PredictOptions(
      Boolean allWords,
      Boolean fullText,
      Boolean cropper,
      String workflowId,
      Boolean rag
  ) {
    this.allWords = allWords == null ? Boolean.FALSE : allWords;
    this.fullText = fullText == null ? Boolean.FALSE : fullText;
    this.cropper = cropper == null ? Boolean.FALSE : cropper;
    this.workflowId = workflowId;
    this.rag = rag == null ? Boolean.FALSE : rag;
  }
}
