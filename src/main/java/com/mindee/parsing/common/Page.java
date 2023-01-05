package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Define a page in the parsed document.
 *
 * @param <T> The prediction model type.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page<T> {

  /**
   * The id of the page. It starts at 0.
   */
  @JsonProperty("id")
  private int pageId;
  /**
   * The orientation which was applied from the original page.
   */
  @JsonProperty("orientation")
  private Orientation orientation;
  /**
   * The prediction model type.
   */
  @JsonProperty("prediction")
  private T prediction;

  @Override
  public String toString() {
    return
        String.format("Page %s%n", this.getPageId()) +
        String.format("------%n") +
        prediction.toString();
  }
}
