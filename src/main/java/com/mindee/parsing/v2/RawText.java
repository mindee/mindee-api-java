package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.StringJoiner;
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
  /**
   * Page Number the text was found on.
   */
  @JsonProperty("pages")
  private List<RawTextPage> pages;

  @Override
  public String toString() {
    if (pages == null || pages.isEmpty()) {
      return "";
    }
    StringJoiner joiner = new StringJoiner("\n\n");
    for (RawTextPage page : pages) {
      joiner.add(page.toString());
    }
    return joiner.toString();

  }
}
