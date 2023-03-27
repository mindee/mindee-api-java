package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import lombok.Getter;

import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * Common inference data.
 *
 * @param <T> Page prediction (could be the same that U).
 * @param <U> Document prediction (could be the same that T).
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Inference<T, U> {

  /**
   * Was a rotation applied to parse the document ?
   */
  @JsonProperty("is_rotation_applied")
  private boolean isRotationApplied;
  /**
   * Type of product.
   */
  @JsonProperty("product")
  private Product product;
  /**
   * The prediction on each pages of the document.
   */
  @JsonProperty("pages")
  private ArrayList<Page<T>> pages;
  /**
   * The prediction on the document level.
   */
  @JsonProperty("prediction")
  private U documentPrediction;

  @Override
  public String toString() {

    String summary =
      String.format("%nInference%n") +
        String.format("#########%n") +
        String.format(":Product: %s v%s%n", getProduct().getName(), getProduct().getVersion()) +
        String.format(":Rotation applied: %s%n", isRotationApplied() ? "Yes" : "No") +
        String.format("%n") +
        String.format("Prediction%n") +
        String.format("==========%n") +
        documentPrediction.toString() +
        String.format("%nPage Predictions%n") +
        String.format("================%n%n") +
        pages.stream().map(Page::toString).collect(Collectors.joining(String.format("%n"))) +
        String.format("%n");

    return SummaryHelper.cleanSummary(summary);
  }
}
