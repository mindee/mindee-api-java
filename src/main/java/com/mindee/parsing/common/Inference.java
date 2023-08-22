package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * Common inference data.
 *
 * @param <PageT> Page prediction (can be the same as DocT).
 * @param <DocT> Document prediction (can be the same as PageT).
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Inference<PageT, DocT> {

  /**
   * Whether a rotation was applied to parse the document.
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
  private List<Page<PageT>> pages;
  /**
   * The prediction on the document level.
   */
  @JsonProperty("prediction")
  private DocT prediction;

  @Override
  public String toString() {
    String summary =
        String.format("%nInference%n")
        + String.format("#########%n")
        + String.format(":Product: %s v%s%n", getProduct().getName(), getProduct().getVersion())
        + String.format(":Rotation applied: %s%n", isRotationApplied() ? "Yes" : "No")
        + String.format("%n")
        + String.format("Prediction%n")
        + String.format("==========%n")
        + prediction.toString()
        + String.format("%nPage Predictions%n")
        + String.format("================%n%n")
        + pages.stream().map(Page::toString).collect(Collectors.joining(String.format("%n")))
        + String.format("%n");
    return SummaryHelper.cleanSummary(summary);
  }
}
