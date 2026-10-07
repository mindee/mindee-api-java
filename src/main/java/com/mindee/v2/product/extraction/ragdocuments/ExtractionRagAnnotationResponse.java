package com.mindee.v2.product.extraction.ragdocuments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.parsing.BaseRagAnnotationResponse;
import com.mindee.v2.product.ProductAttributes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response for a RAG document.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@ProductAttributes(slug = "extraction")
public class ExtractionRagAnnotationResponse extends BaseRagAnnotationResponse {

  /**
   * Model identifier linked to the RAG document.
   */
  @JsonProperty("model_id")
  private String modelId;

  /**
   * Number of times this document was used in an inference.
   */
  @JsonProperty("total_matches")
  private int totalMatches;

  /**
   * Date and time of the latest matching inference, if any.
   */
  @JsonProperty("last_match_at")
  private String lastMatchAt;

  /**
   * Annotation metadata associated with the document.
   */
  @JsonProperty("annotation")
  private RagAnnotation annotation;
}
