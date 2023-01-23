package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a page.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrPage {

  @JsonProperty("all_words")
  private List<Word> words = new ArrayList<>();
}
