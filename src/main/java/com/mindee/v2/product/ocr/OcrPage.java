package com.mindee.v2.product.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * OCR result for a single page.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class OcrPage {
  /**
   * Full text content extracted from the document page.
   */
  @JsonProperty("content")
  private String content;

  /**
   * List of words extracted from the document page.
   */
  @JsonProperty("words")
  private ArrayList<OcrWord> words;
}
