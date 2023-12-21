package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.Getter;

/**
 * Define a page in the parsed document.
 *
 * @param <TPagePrediction> The prediction model type.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page<TPagePrediction> {

  /**
   * The id of the page. It starts at 0.
   */
  @JsonProperty("id")
  private int pageId;
  /**
   * Optional information.
   */
  @JsonProperty("extras")
  private Extras extras;
  /**
   * The orientation which was applied from the original page.
   */
  @JsonProperty("orientation")
  private Orientation orientation;
  /**
   * The prediction model type.
   */
  @JsonProperty("prediction")
  private TPagePrediction prediction;

  @Override
  public String toString() {
    return String.format("Page %s%n", this.getPageId())
        + String.format("------%n")
        + prediction.toString();
  }

  /**
   * Returns <code>true</code> if there are no predictions in this page.
   * Accessing prediction values when this is <code>true</code> may result in a {@link NullPointerException}
   */
  public boolean isPredictionEmpty() {
    //
    // Yes, this is absolutely disgusting.
    // Unfortunately, fixing it requires bounding the TPagePrediction type
    // to Prediction, which will break backwards compatibility with CustomV1 stuff.
    //
    // Let's wait for a new release of Invoice to make a breaking change.
    //
    try {
      Method fooMethod = prediction.getClass().getMethod("isEmpty");
      try {
        return (boolean) fooMethod.invoke(prediction);
      } catch (IllegalAccessException | InvocationTargetException err) {
        return false;
      }
    } catch (NoSuchMethodException err) {
      return false;
    }
  }
}
