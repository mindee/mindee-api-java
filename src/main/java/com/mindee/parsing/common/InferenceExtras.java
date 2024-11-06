package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Inference-level optional info.
 */
@Setter
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class InferenceExtras {
  /**
   * Full Text OCR result.
   */
  private String fullTextOcr;
}
