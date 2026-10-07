package com.mindee.v2.product.extraction.ragdocuments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base class for annotated fields.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class AnnotatedBaseField {

  /**
   * When true, use the RAG information for the final result. When false, use the Data Schema
   * information.
   */
  @JsonProperty("selected")
  private boolean selected;

  /**
   * Guidelines or instructions for processing this field.
   */
  @JsonProperty("guidelines")
  private String guidelines;
}
