package com.mindee.v2.product.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Result of the OCR utility inference.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class OcrResult {
  /**
   * List of OCR results for each page in the document.
   */
  @JsonProperty("pages")
  private ArrayList<OcrPage> pages;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    joiner.add("Pages\n======");
    for (OcrPage item : pages) {
      joiner.add(item.toString());
    }
    return joiner.toString();
  }
}
