package com.mindee.product.fr.payslip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Information about the pay period.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipV3PayPeriod extends BaseField {

  /**
   * The end date of the pay period.
   */
  @JsonProperty("end_date")
  String endDate;
  /**
   * The month of the pay period.
   */
  @JsonProperty("month")
  String month;
  /**
   * The date of payment for the pay period.
   */
  @JsonProperty("payment_date")
  String paymentDate;
  /**
   * The start date of the pay period.
   */
  @JsonProperty("start_date")
  String startDate;
  /**
   * The year of the pay period.
   */
  @JsonProperty("year")
  String year;

  public boolean isEmpty() {
    return (
        (endDate == null || endDate.isEmpty())
        && (month == null || month.isEmpty())
        && (paymentDate == null || paymentDate.isEmpty())
        && (startDate == null || startDate.isEmpty())
        && (year == null || year.isEmpty())
      );
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :End Date: %s%n", printable.get("endDate"))
        + String.format("  :Month: %s%n", printable.get("month"))
        + String.format("  :Payment Date: %s%n", printable.get("paymentDate"))
        + String.format("  :Start Date: %s%n", printable.get("startDate"))
        + String.format("  :Year: %s%n", printable.get("year"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("End Date: %s", printable.get("endDate"))
      + String.format(", Month: %s", printable.get("month"))
      + String.format(", Payment Date: %s", printable.get("paymentDate"))
      + String.format(", Start Date: %s", printable.get("startDate"))
      + String.format(", Year: %s", printable.get("year"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("endDate", SummaryHelper.formatForDisplay(this.endDate, null));
    printable.put("month", SummaryHelper.formatForDisplay(this.month, null));
    printable.put("paymentDate", SummaryHelper.formatForDisplay(this.paymentDate, null));
    printable.put("startDate", SummaryHelper.formatForDisplay(this.startDate, null));
    printable.put("year", SummaryHelper.formatForDisplay(this.year, null));
    return printable;
  }
}
