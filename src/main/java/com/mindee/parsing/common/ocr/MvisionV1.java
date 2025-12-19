package com.mindee.parsing.common.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * List all pages that have ocr results.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MvisionV1 {

  @JsonProperty("pages")
  private List<OcrPage> pages = new ArrayList<>();

  @Override
  public String toString() {
    return this
      .getPages()
      .stream()
      .map(OcrPage::toString)
      .collect(Collectors.joining(String.format("%n")));
  }
}
