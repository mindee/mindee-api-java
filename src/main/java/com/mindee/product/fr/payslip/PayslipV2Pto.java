package com.mindee.product.fr.payslip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Information about paid time off.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipV2Pto extends BaseField {

  /**
   * The amount of paid time off accrued in this period.
   */
  @JsonProperty("accrued_this_period")
  Double accruedThisPeriod;
  /**
   * The balance of paid time off at the end of the period.
   */
  @JsonProperty("balance_end_of_period")
  Double balanceEndOfPeriod;
  /**
   * The amount of paid time off used in this period.
   */
  @JsonProperty("used_this_period")
  Double usedThisPeriod;

  public boolean isEmpty() {
    return (accruedThisPeriod == null && balanceEndOfPeriod == null && usedThisPeriod == null);
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Accrued This Period: %s%n", printable.get("accruedThisPeriod"))
      + String.format("  :Balance End of Period: %s%n", printable.get("balanceEndOfPeriod"))
      + String.format("  :Used This Period: %s%n", printable.get("usedThisPeriod"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Accrued This Period: %s", printable.get("accruedThisPeriod"))
      + String.format(", Balance End of Period: %s", printable.get("balanceEndOfPeriod"))
      + String.format(", Used This Period: %s", printable.get("usedThisPeriod"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("accruedThisPeriod", SummaryHelper.formatAmount(this.accruedThisPeriod));
    printable.put("balanceEndOfPeriod", SummaryHelper.formatAmount(this.balanceEndOfPeriod));
    printable.put("usedThisPeriod", SummaryHelper.formatAmount(this.usedThisPeriod));
    return printable;
  }
}
