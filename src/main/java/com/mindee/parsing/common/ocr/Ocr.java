package com.mindee.parsing.common.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Ocr result information.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ocr {

  @JsonProperty("mvision-v1")
  private MvisionV1 mVisionV1;

  @Override
  public String toString() {
    return this.mVisionV1.toString();
  }
}
