package com.mindee.parsing.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.Page;
import lombok.Getter;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * The receipt V4 inference.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@EndpointInfo(endpointName = "expense_receipts", version = "4")
public class ReceiptV4Inference extends Inference {

  /**
   * The prediction on each pages of the document.
   */
  @JsonProperty("pages")
  private ArrayList<Page<ReceiptV4DocumentPrediction>> pagesPrediction;
  /**
   * The prediction on the document level.
   */
  @JsonProperty("prediction")
  private ReceiptV4DocumentPrediction documentPrediction;

  @Override
  public String toString() {
    String summary =
      super.toString() +
        String.format("%n") +
        String.format("Prediction%n") +
        String.format("==========%n") +
        documentPrediction.toString() +
        String.format("%nPage Predictions%n") +
        String.format("================%n%n") +
        pagesPrediction.stream().map(Page::toString).collect(Collectors.joining(String.format("%n"))) +
        String.format("%n");

    return SummaryHelper.cleanSummary(summary);
  }
}
