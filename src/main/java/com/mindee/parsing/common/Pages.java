package com.mindee.parsing.common;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * List of all pages in the inference.
 * @param <TPagePrediction>
 */
public class Pages<TPagePrediction> extends ArrayList<Page<TPagePrediction>> {
  @Override
  public String toString() {
    return this.stream()
        .map(Page::toString)
        .collect(Collectors.joining(String.format("%n")));
  }

  /**
   * Returns whether any predictions are set.
   */
  public boolean hasPrediction() {
    return (!this.isEmpty() && !this.get(0).isPredictionEmpty());
  }
}
