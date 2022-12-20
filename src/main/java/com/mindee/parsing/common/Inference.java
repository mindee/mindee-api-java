package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * The inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Inference {

  /**
   * Was a rotation applied to parse the document ?
   */
  @JsonProperty("is_rotation_applied")
  private boolean isRotationApplied;
  /**
   * Type of product.
   */
  @JsonProperty("product")
  private Product product;
}
