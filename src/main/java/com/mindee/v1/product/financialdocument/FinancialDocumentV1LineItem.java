package com.mindee.v1.product.financialdocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v1.parsing.SummaryHelperV1;
import com.mindee.v1.parsing.standard.BaseField;
import com.mindee.v1.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * List of line item present on the document.
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
   * The item quantity.
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
   * The item unit of measure.
   */
  @JsonProperty("unit_measure")
  String unitMeasure;
  /**
   * The item unit price.
   */
  @JsonProperty("unit_price")
  Double unitPrice;

  public boolean isEmpty() {
    return ((description == null || description.isEmpty())
      && (productCode == null || productCode.isEmpty())
      && quantity == null
      && taxAmount == null
      && taxRate == null
      && totalAmount == null
      && (unitMeasure == null || unitMeasure.isEmpty())
      && unitPrice == null);
  }

  private Map<String, String> tablePrintableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("description", SummaryHelperV1.formatForDisplay(this.description, 36));
    printable.put("productCode", SummaryHelperV1.formatForDisplay(this.productCode, null));
    printable.put("quantity", SummaryHelperV1.formatAmount(this.quantity));
    printable.put("taxAmount", SummaryHelperV1.formatAmount(this.taxAmount));
    printable.put("taxRate", SummaryHelperV1.formatAmount(this.taxRate));
    printable.put("totalAmount", SummaryHelperV1.formatAmount(this.totalAmount));
    printable.put("unitMeasure", SummaryHelperV1.formatForDisplay(this.unitMeasure, null));
    printable.put("unitPrice", SummaryHelperV1.formatAmount(this.unitPrice));
    return printable;
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.tablePrintableValues();
    return String.format("| %-36s ", printable.get("description"))
      + String.format("| %-12s ", printable.get("productCode"))
      + String.format("| %-8s ", printable.get("quantity"))
      + String.format("| %-10s ", printable.get("taxAmount"))
      + String.format("| %-12s ", printable.get("taxRate"))
      + String.format("| %-12s ", printable.get("totalAmount"))
      + String.format("| %-15s ", printable.get("unitMeasure"))
      + String.format("| %-10s |", printable.get("unitPrice"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Description: %s", printable.get("description"))
      + String.format(", Product code: %s", printable.get("productCode"))
      + String.format(", Quantity: %s", printable.get("quantity"))
      + String.format(", Tax Amount: %s", printable.get("taxAmount"))
      + String.format(", Tax Rate (%%): %s", printable.get("taxRate"))
      + String.format(", Total Amount: %s", printable.get("totalAmount"))
      + String.format(", Unit of measure: %s", printable.get("unitMeasure"))
      + String.format(", Unit Price: %s", printable.get("unitPrice"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("description", SummaryHelperV1.formatForDisplay(this.description, null));
    printable.put("productCode", SummaryHelperV1.formatForDisplay(this.productCode, null));
    printable.put("quantity", SummaryHelperV1.formatAmount(this.quantity));
    printable.put("taxAmount", SummaryHelperV1.formatAmount(this.taxAmount));
    printable.put("taxRate", SummaryHelperV1.formatAmount(this.taxRate));
    printable.put("totalAmount", SummaryHelperV1.formatAmount(this.totalAmount));
    printable.put("unitMeasure", SummaryHelperV1.formatForDisplay(this.unitMeasure, null));
    printable.put("unitPrice", SummaryHelperV1.formatAmount(this.unitPrice));
    return printable;
  }
}
