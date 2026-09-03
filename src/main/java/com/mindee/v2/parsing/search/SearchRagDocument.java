package com.mindee.v2.parsing.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Individual RAG document information.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchRagDocument {

  /**
   * Unique identifier of the RAG document.
   */
  @JsonProperty("id")
  private String id;

  /**
   * Model identifier linked to the RAG document.
   */
  @JsonProperty("model_id")
  private String modelId;

  /**
   * Original filename of the uploaded document.
   */
  @JsonProperty("filename")
  private String filename;

  /**
   * Date and time of the document creation.
   */
  @JsonProperty("created_at")
  private OffsetDateTime createdAt;

  /**
   * Number of times this document was used in an inference.
   */
  @JsonProperty("total_matches")
  private int totalMatches;

  /**
   * Date and time of the latest matching inference, if any.
   */
  @JsonProperty("last_match_at")
  private OffsetDateTime lastMatchAt;

  /**
   * Current status of the RAG document.
   */
  @JsonProperty("status")
  private String status;
}
