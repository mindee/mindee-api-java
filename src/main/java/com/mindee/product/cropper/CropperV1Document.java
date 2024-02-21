package com.mindee.product.cropper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.common.Prediction;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Cropper, API version 1.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CropperV1Document extends Prediction {


  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public String toString() {
    return "";
  }
}
