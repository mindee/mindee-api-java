package com.mindee.v2.product.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.geometry.Polygon;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * OCR result for a single word extracted from the document page.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class OcrWord {
  /**
   * Text content of the word.
   */
  @JsonProperty("content")
  private String content;

  /**
   * Position information as a list of points in clockwise order.
   */
  @JsonProperty("polygon")
  private Polygon polygon;
}
