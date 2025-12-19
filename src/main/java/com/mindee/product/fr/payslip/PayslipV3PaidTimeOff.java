package com.mindee.product.fr.payslip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Information about paid time off.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipV3PaidTimeOff extends BaseField implements LineItemField {

  /**
   * The amount of paid time off accrued in the period.
   */
  @JsonProperty("accrued")
  Double accrued;
  /**
   * The paid time off period.
   */
  @JsonProperty("period")
  String period;
  /**
   * The type of paid time off.
   */
  @JsonProperty("pto_type")
  String ptoType;
  /**
   * The remaining amount of paid time off at the end of the period.
   */
  @JsonProperty("remaining")
  Double remaining;
  /**
   * The amount of paid time off used in the period.
   */
  @JsonProperty("used")
  Double used;

  public boolean isEmpty() {
    return (accrued == null
      && (period == null || period.isEmpty())
      && (ptoType == null || ptoType.isEmpty())
      && remaining == null
      && used == null);
  }

  private Map<String, String> tablePrintableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("accrued", SummaryHelper.formatAmount(this.accrued));
    printable.put("period", SummaryHelper.formatForDisplay(this.period, 6));
    printable.put("ptoType", SummaryHelper.formatForDisplay(this.ptoType, 11));
    printable.put("remaining", SummaryHelper.formatAmount(this.remaining));
    printable.put("used", SummaryHelper.formatAmount(this.used));
    return printable;
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.tablePrintableValues();
    return String.format("| %-9s ", printable.get("accrued"))
      + String.format("| %-6s ", printable.get("period"))
      + String.format("| %-11s ", printable.get("ptoType"))
      + String.format("| %-9s ", printable.get("remaining"))
      + String.format("| %-9s |", printable.get("used"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Accrued: %s", printable.get("accrued"))
      + String.format(", Period: %s", printable.get("period"))
      + String.format(", Type: %s", printable.get("ptoType"))
      + String.format(", Remaining: %s", printable.get("remaining"))
      + String.format(", Used: %s", printable.get("used"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("accrued", SummaryHelper.formatAmount(this.accrued));
    printable.put("period", SummaryHelper.formatForDisplay(this.period, null));
    printable.put("ptoType", SummaryHelper.formatForDisplay(this.ptoType, null));
    printable.put("remaining", SummaryHelper.formatAmount(this.remaining));
    printable.put("used", SummaryHelper.formatAmount(this.used));
    return printable;
  }
}
