package com.mindee.product.ocrdemo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.common.Prediction;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for OCR Demo, API version 1.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrDemoV1Document extends Prediction {


  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public String toString() {
    return "";
  }
}
