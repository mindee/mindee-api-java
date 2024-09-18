package com.mindee.product.nutritionfactslabel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The size of a single serving of the product.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutritionFactsLabelV1ServingSize extends BaseField {

  /**
   * The amount of a single serving.
   */
  @JsonProperty("amount")
  Double amount;
  /**
   * The unit for the amount of a single serving.
   */
  @JsonProperty("unit")
  String unit;

  public boolean isEmpty() {
    return (
        amount == null
        && (unit == null || unit.isEmpty())
      );
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Amount: %s%n", printable.get("amount"))
        + String.format("  :Unit: %s%n", printable.get("unit"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Amount: %s", printable.get("amount"))
      + String.format(", Unit: %s", printable.get("unit"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put(
        "amount",
        SummaryHelper.formatAmount(this.amount)
    );
    printable.put("unit", SummaryHelper.formatForDisplay(this.unit, null));
    return printable;
  }
}
