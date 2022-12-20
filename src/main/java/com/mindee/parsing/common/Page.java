package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page<T> {

  @JsonProperty("id")
  private int pageId;
  @JsonProperty("orientation")
  private Orientation orientation;
  @JsonProperty("prediction")
  private T prediction;

  @Override
  public String toString() {
    return
      String.format("-------%n") +
        String.format("Page %s%n", this.getPageId()) +
        String.format("-------%n") +
        prediction.toString();
  }
}
