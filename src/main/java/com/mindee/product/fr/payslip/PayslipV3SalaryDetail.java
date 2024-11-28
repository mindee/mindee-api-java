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
 * Detailed information about the earnings.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipV3SalaryDetail extends BaseField implements LineItemField {

  /**
   * The amount of the earning.
   */
  @JsonProperty("amount")
  Double amount;
  /**
   * The base rate value of the earning.
   */
  @JsonProperty("base")
  Double base;
  /**
   * The description of the earnings.
   */
  @JsonProperty("description")
  String description;
  /**
   * The number of units in the earning.
   */
  @JsonProperty("number")
  Double number;
  /**
   * The rate of the earning.
   */
  @JsonProperty("rate")
  Double rate;

  public boolean isEmpty() {
    return (
        amount == null
        && base == null
        && (description == null || description.isEmpty())
        && number == null
        && rate == null
      );
  }

  private Map<String, String> tablePrintableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put(
        "amount",
        SummaryHelper.formatAmount(this.amount)
    );
    printable.put(
        "base",
        SummaryHelper.formatAmount(this.base)
    );
    printable.put("description", SummaryHelper.formatForDisplay(this.description, 36));
    printable.put(
        "number",
        SummaryHelper.formatAmount(this.number)
    );
    printable.put(
        "rate",
        SummaryHelper.formatAmount(this.rate)
    );
    return printable;
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.tablePrintableValues();
    return String.format("| %-12s ", printable.get("amount"))
      + String.format("| %-9s ", printable.get("base"))
      + String.format("| %-36s ", printable.get("description"))
      + String.format("| %-6s ", printable.get("number"))
      + String.format("| %-9s |", printable.get("rate"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Amount: %s", printable.get("amount"))
      + String.format(", Base: %s", printable.get("base"))
      + String.format(", Description: %s", printable.get("description"))
      + String.format(", Number: %s", printable.get("number"))
      + String.format(", Rate: %s", printable.get("rate"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put(
        "amount",
        SummaryHelper.formatAmount(this.amount)
    );
    printable.put(
        "base",
        SummaryHelper.formatAmount(this.base)
    );
    printable.put("description", SummaryHelper.formatForDisplay(this.description, null));
    printable.put(
        "number",
        SummaryHelper.formatAmount(this.number)
    );
    printable.put(
        "rate",
        SummaryHelper.formatAmount(this.rate)
    );
    return printable;
  }
}
