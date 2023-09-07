package com.mindee.product.cropper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Cropper, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class CropperV1Document {


  @Override
  public String toString() {
    return "";
  }
}
