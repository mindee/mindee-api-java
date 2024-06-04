package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * Common inference data.
 *
 * @param <TPagePrediction> Page prediction (can be the same as TDocumentPrediction).
 * @param <TDocumentPrediction> Document prediction (can be the same as TPagePrediction).
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Inference<TPagePrediction, TDocumentPrediction extends Prediction> {

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
  protected Pages<TPagePrediction> pages;
  /**
   * The prediction on the document level.
   */
  @JsonProperty("prediction")
  private TDocumentPrediction prediction;
  /**
   * Optional information.
   */
  @JsonProperty("extras")
  private Extras extras;

  private InferenceExtras inferenceExtras;

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
        + prediction.toString();
    if (pages.hasPrediction()) {
      summary += String.format("%nPage Predictions%n")
        + String.format("================%n%n")
        + pages.toString();
    }
    summary += String.format("%n");
    return SummaryHelper.cleanSummary(summary);
  }

  public InferenceExtras getInferenceExtras(){
    if (this.inferenceExtras == null) {
      this.inferenceExtras = new InferenceExtras();
    }
    if (this.extras != null) {
      if (this.pages != null && !this.pages.isEmpty() && this.inferenceExtras.getFullTextOcr() == null) {
        if (this.pages.get(0).getExtras() != null && this.pages.get(0).getExtras().getFullTextOcr() != null) {
          this.inferenceExtras.setFullTextOcr(String.join("\n", this.pages.stream().map(page -> page.getExtras().getFullTextOcr().getContent()).collect(
            Collectors.joining("\n"))));
        }
      }
    }
    return this.inferenceExtras;
  }
}
