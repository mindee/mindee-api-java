package com.mindee.product.receiptsitemsclassifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * A list of fields about individual items or services being billed.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptsItemsClassifierV1LineItem extends BaseField implements LineItemField {

  /**
   * The class or type of item.
   */
  @JsonProperty("item_classification")
  String itemClassification;
  /**
   * The name or description of a specific item or product being invoiced.
   */
  @JsonProperty("item_name")
  String itemName;
  /**
   * The number of units of a specific item that were purchased or sold.
   */
  @JsonProperty("quantity")
  Double quantity;
  /**
   * The total cost of a specific item or service, including any applicable taxes or discounts.
   */
  @JsonProperty("total_price")
  Double totalPrice;

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.printableValues();
    return String.format("| %-19s ", printable.get("itemClassification"))
      + String.format("| %-9s ", printable.get("itemName"))
      + String.format("| %-8s ", printable.get("quantity"))
      + String.format("| %-5s |", printable.get("totalPrice"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Item Classification: %s", printable.get("itemClassification"))
      + String.format(", Item Name: %s", printable.get("itemName"))
      + String.format(", Quantity: %s", printable.get("quantity"))
      + String.format(", Price: %s", printable.get("totalPrice"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("itemClassification", SummaryHelper.formatForDisplay(this.itemClassification, null));
    printable.put("itemName", SummaryHelper.formatForDisplay(this.itemName, null));
    printable.put(
        "quantity",
        SummaryHelper.formatAmount(this.quantity)
    );
    printable.put(
        "totalPrice",
        SummaryHelper.formatAmount(this.totalPrice)
    );
    return printable;
  }
}
