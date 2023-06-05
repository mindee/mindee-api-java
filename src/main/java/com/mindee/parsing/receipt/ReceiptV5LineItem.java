package com.mindee.parsing.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.LineItemField;
import java.util.HashMap;
import lombok.Getter;

/**
 * Full extraction of lines, including: description, quantity, unit price and total.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptV5LineItem extends LineItemField {

  /**
   * The item description.
   */
  @JsonProperty("description")
  String description;
  /**
   * The item quantity.
   */
  @JsonProperty("quantity")
  Double quantity;
  /**
   * The item total amount.
   */
  @JsonProperty("total_amount")
  Double totalAmount;
  /**
   * The item unit price.
   */
  @JsonProperty("unit_price")
  Double unitPrice;

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    HashMap<String, String> printable = this.printableValues();
    return String.format("| %-36s ", printable.get("description"))
      + String.format("| %-8s ", printable.get("quantity"))
      + String.format("| %-12s ", printable.get("totalAmount"))
      + String.format("| %-10s |", printable.get("unitPrice"));
  }

  @Override
  public String toString() {
    HashMap<String, String> printable = this.printableValues();
    return String.format("Description: %s", printable.get("description"))
      + String.format("Quantity: %s", printable.get("quantity"))
      + String.format("Total Amount: %s", printable.get("totalAmount"))
      + String.format("Unit Price: %s", printable.get("unitPrice"));
  }

  protected HashMap<String, String> printableValues() {
    HashMap<String, String> printable = new HashMap<>();

    String descriptionSummary = (this.description != null ? this.description : "");
    if (descriptionSummary.length() > 33) {
      descriptionSummary = descriptionSummary.substring(0, 33) + "...";
    }
    printable.put("description", descriptionSummary);
    printable.put(
        "quantity",
        this.quantity != null ? SummaryHelper.formatAmount(this.quantity) : ""
    );
    printable.put(
        "totalAmount",
        this.totalAmount != null ? SummaryHelper.formatAmount(this.totalAmount) : ""
    );
    printable.put(
        "unitPrice",
        this.unitPrice != null ? SummaryHelper.formatAmount(this.unitPrice) : ""
    );
    return printable;
  }
}
