package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Raw text extracted from the page.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class RawTextPage {
  /**
   * Page content as a single string.
   */
  @JsonProperty("content")
  private String content;

  /**
   * Page contents as a string.
   */
  @Override
  public String toString() {
    if (content == null) {
      return "";
    }
    return content;
  }
}
