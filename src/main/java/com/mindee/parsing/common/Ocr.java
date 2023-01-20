package com.mindee.parsing.common;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.stream.Collectors;

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
    return
        mVisionV1.getPages()
          .stream()
          .map(p -> p.getAllWords()
            .stream().map(w -> w.getText())
            .collect(
              Collectors.joining(" ")))
          .collect(Collectors.joining(" "));
  }
}


