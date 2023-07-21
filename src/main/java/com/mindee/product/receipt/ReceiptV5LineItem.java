package com.mindee.product.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * List of line item details.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptV5LineItem extends BaseField implements LineItemField {

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
    Map<String, String> printable = this.printableValues();
    return String.format("| %-36s ", printable.get("description"))
      + String.format("| %-8s ", printable.get("quantity"))
      + String.format("| %-12s ", printable.get("totalAmount"))
      + String.format("| %-10s |", printable.get("unitPrice"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Description: %s", printable.get("description"))
      + String.format(", Quantity: %s", printable.get("quantity"))
      + String.format(", Total Amount: %s", printable.get("totalAmount"))
      + String.format(", Unit Price: %s", printable.get("unitPrice"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

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
