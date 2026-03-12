package com.mindee.v2.product.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.v2.CommonResponse;
import lombok.Getter;

/**
 * Response for an OCR utility inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrResponse extends CommonResponse {

  /**
   * The inference result for an OCR utility request.
   */
  @JsonProperty("inference")
  private OcrInference inference;
}
