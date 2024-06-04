package com.mindee.parsing.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class InferenceExtras {
  /**
   * Full Text OCR result.
   */
  private String fullTextOcr;

}
