package com.mindee.product.multireceiptsdetector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.PositionField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Multi Receipts Detector API version 1.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultiReceiptsDetectorV1Document extends Prediction {

  /**
   * Positions of the receipts on the document.
   */
  @JsonProperty("receipts")
  protected List<PositionField> receipts = new ArrayList<>();

  @Override
  public boolean isEmpty() {
    return (
      (this.receipts == null || this.receipts.isEmpty())
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    String receipts = SummaryHelper.arrayToString(
        this.getReceipts(),
        "%n                   "
    );
    outStr.append(
        String.format(":List of Receipts: %s%n", receipts)
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
