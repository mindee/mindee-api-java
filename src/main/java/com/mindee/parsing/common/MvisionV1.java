package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * List all pages that have ocr results.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MvisionV1 {

  @JsonProperty("pages")
  private List<OcrPage> pages = new ArrayList<>();
}
