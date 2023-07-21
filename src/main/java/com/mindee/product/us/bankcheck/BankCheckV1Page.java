package com.mindee.product.us.bankcheck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.PositionField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Page data for Bank Check, API version 1.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankCheckV1Page extends BankCheckV1Document {

  /**
   * The position of the check on the document.
   */
  @JsonProperty("check_position")
  private PositionField checkPosition;
  /**
   * List of signature positions
   */
  @JsonProperty("signatures_positions")
  private List<PositionField> signaturesPositions = new ArrayList<>();

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Check Position: %s%n", this.getCheckPosition())
    );
    String signaturesPositions = SummaryHelper.arrayToString(
        this.getSignaturesPositions(),
        "%n                       "
    );
    outStr.append(
        String.format(":Signature Positions: %s%n", signaturesPositions)
    );
    outStr.append(super.toString());
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
