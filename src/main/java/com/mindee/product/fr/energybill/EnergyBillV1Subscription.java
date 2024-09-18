package com.mindee.product.fr.energybill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The subscription details fee for the energy service.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnergyBillV1Subscription extends BaseField implements LineItemField {

  /**
   * Description or details of the subscription.
   */
  @JsonProperty("description")
  String description;
  /**
   * The end date of the subscription.
   */
  @JsonProperty("end_date")
  String endDate;
  /**
   * The start date of the subscription.
   */
  @JsonProperty("start_date")
  String startDate;
  /**
   * The rate of tax applied to the total cost.
   */
  @JsonProperty("tax_rate")
  Double taxRate;
  /**
   * The total cost of subscription.
   */
  @JsonProperty("total")
  Double total;
  /**
   * The price per unit of subscription.
   */
  @JsonProperty("unit_price")
  Double unitPrice;

  public boolean isEmpty() {
    return (
        (description == null || description.isEmpty())
        && (endDate == null || endDate.isEmpty())
        && (startDate == null || startDate.isEmpty())
        && taxRate == null
        && total == null
        && unitPrice == null
      );
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.printableValues();
    return String.format("| %-36s ", printable.get("description"))
      + String.format("| %-10s ", printable.get("endDate"))
      + String.format("| %-10s ", printable.get("startDate"))
      + String.format("| %-8s ", printable.get("taxRate"))
      + String.format("| %-9s ", printable.get("total"))
      + String.format("| %-10s |", printable.get("unitPrice"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Description: %s", printable.get("description"))
      + String.format(", End Date: %s", printable.get("endDate"))
      + String.format(", Start Date: %s", printable.get("startDate"))
      + String.format(", Tax Rate: %s", printable.get("taxRate"))
      + String.format(", Total: %s", printable.get("total"))
      + String.format(", Unit Price: %s", printable.get("unitPrice"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    String descriptionSummary = (this.description != null ? this.description : "");
    if (descriptionSummary.length() >= 36) {
      descriptionSummary = descriptionSummary.substring(0, 33) + "...";
    }
    printable.put("description", descriptionSummary);
    printable.put("endDate", SummaryHelper.formatForDisplay(this.endDate, 10));
    printable.put("startDate", SummaryHelper.formatForDisplay(this.startDate, null));
    printable.put(
        "taxRate",
        SummaryHelper.formatAmount(this.taxRate)
    );
    printable.put(
        "total",
        SummaryHelper.formatAmount(this.total)
    );
    printable.put(
        "unitPrice",
        SummaryHelper.formatAmount(this.unitPrice)
    );
    return printable;
  }
}
