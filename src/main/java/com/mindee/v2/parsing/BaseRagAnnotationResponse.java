package com.mindee.v2.parsing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Base class for all RAG document responses from the V2 API.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class BaseRagAnnotationResponse extends BaseResponse {

  /**
   * Unique identifier of the RAG document.
   */
  @JsonProperty("id")
  private String id;

  /**
   * Original filename of the uploaded document.
   */
  @JsonProperty("filename")
  private String filename;

  /**
   * Date and time of the document creation.
   */
  @JsonProperty("created_at")
  private String createdAt;

  /**
   * Current status of the RAG document.
   */
  @JsonProperty("status")
  private String status;
}
