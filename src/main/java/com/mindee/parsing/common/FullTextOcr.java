package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Full Text OCR result.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class FullTextOcr {
  /**
   * List of positions within the image.
   */
  @JsonProperty("content")
  private String content;
  /**
   * Language used in the text.
   */
  @JsonProperty("language")
  private String language;
}
