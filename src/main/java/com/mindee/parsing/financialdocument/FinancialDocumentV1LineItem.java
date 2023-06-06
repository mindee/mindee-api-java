package com.mindee.parsing.financialdocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.BaseField;
import com.mindee.parsing.common.field.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * List of line item details.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialDocumentV1LineItem extends BaseField implements LineItemField {

  /**
   * The item description.
   */
  @JsonProperty("description")
  String description;
  /**
   * The product code referring to the item.
   */
  @JsonProperty("product_code")
  String productCode;
  /**
   * The item quantity
   */
  @JsonProperty("quantity")
  Double quantity;
  /**
   * The item tax amount.
   */
  @JsonProperty("tax_amount")
  Double taxAmount;
  /**
   * The item tax rate in percentage.
   */
  @JsonProperty("tax_rate")
  Double taxRate;
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
      + String.format("| %-12s ", printable.get("productCode"))
      + String.format("| %-8s ", printable.get("quantity"))
      + String.format("| %-10s ", printable.get("taxAmount"))
      + String.format("| %-12s ", printable.get("taxRate"))
      + String.format("| %-12s ", printable.get("totalAmount"))
      + String.format("| %-10s |", printable.get("unitPrice"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Description: %s", printable.get("description"))
      + String.format("Product code: %s", printable.get("productCode"))
      + String.format("Quantity: %s", printable.get("quantity"))
      + String.format("Tax Amount: %s", printable.get("taxAmount"))
      + String.format("Tax Rate (%%): %s", printable.get("taxRate"))
      + String.format("Total Amount: %s", printable.get("totalAmount"))
      + String.format("Unit Price: %s", printable.get("unitPrice"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    String descriptionSummary = (this.description != null ? this.description : "");
    if (descriptionSummary.length() > 33) {
      descriptionSummary = descriptionSummary.substring(0, 33) + "...";
    }
    printable.put("description", descriptionSummary);
    printable.put("productCode", this.productCode != null ? this.productCode : "");
    printable.put(
        "quantity",
        this.quantity != null ? SummaryHelper.formatAmount(this.quantity) : ""
    );
    printable.put(
        "taxAmount",
        this.taxAmount != null ? SummaryHelper.formatAmount(this.taxAmount) : ""
    );
    printable.put(
        "taxRate",
        this.taxRate != null ? SummaryHelper.formatAmount(this.taxRate) : ""
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
