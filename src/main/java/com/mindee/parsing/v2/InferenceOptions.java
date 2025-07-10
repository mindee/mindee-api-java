package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

/**
 * Option response for V2 API inference.
 */
@Getter
public final class InferenceOptions {

  @JsonProperty("raw_texts")
  private List<RawText> rawTexts;
}
