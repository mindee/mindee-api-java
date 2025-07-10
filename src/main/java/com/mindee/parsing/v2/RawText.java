package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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

  @JsonProperty("page")
  private Integer page;

  @JsonProperty("content")
  private String content;
}
