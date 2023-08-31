package com.mindee.product.multireceiptsdetector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.PositionField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Multi Receipts Detector, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultiReceiptsDetectorV1Document {

  /**
   * Positions of the receipts on the document.
   */
  @JsonProperty("receipts")
  private List<PositionField> receipts = new ArrayList<>();

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
