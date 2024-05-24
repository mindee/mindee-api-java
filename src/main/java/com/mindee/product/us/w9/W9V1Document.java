package com.mindee.product.us.w9;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.common.Prediction;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * W9 API version 1.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class W9V1Document extends Prediction {


  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public String toString() {
    return "";
  }
}
