package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Retrieval-Augmented Generation info class.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rag {
  /**
   * The document ID that was matched.
   */
  @Setter
  @JsonProperty("matching_document_id")
  private String matchingDocumentId;
}
