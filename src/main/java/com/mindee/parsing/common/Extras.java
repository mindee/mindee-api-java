package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Optional information.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Extras {
  /**
   * Cropping result.
   */
  @JsonProperty("cropper")
  private Cropper cropper;
  /**
   * Full Text OCR result.
   */
  @Setter
  @JsonProperty("full_text_ocr")
  private FullTextOcr fullTextOcr;
}
