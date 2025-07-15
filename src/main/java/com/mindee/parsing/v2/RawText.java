package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Raw text as found in the document.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class RawText {
  /*
   * Page Number the text was found on.
   */
  @JsonProperty("page")
  private Integer page;

  /*
   * Content of the raw text.
   */
  @JsonProperty("content")
  private String content;
}
