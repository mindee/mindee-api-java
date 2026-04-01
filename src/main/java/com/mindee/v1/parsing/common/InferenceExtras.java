package com.mindee.v1.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
  /**
   * Retrieval-Augmented Generation results.
   */
  @JsonProperty("rag")
  private Rag rag;
}
