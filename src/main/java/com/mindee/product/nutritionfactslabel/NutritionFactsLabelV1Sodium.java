package com.mindee.product.nutritionfactslabel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The amount of sodium in the product.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutritionFactsLabelV1Sodium extends BaseField {

  /**
   * DVs are the recommended amounts of sodium to consume or not to exceed each day.
   */
  @JsonProperty("daily_value")
  Double dailyValue;
  /**
   * The amount of sodium per 100g of the product.
   */
  @JsonProperty("per_100g")
  Double per100G;
  /**
   * The amount of sodium per serving of the product.
   */
  @JsonProperty("per_serving")
  Double perServing;
  /**
   * The unit of measurement for the amount of sodium.
   */
  @JsonProperty("unit")
  String unit;

  public boolean isEmpty() {
    return (
        dailyValue == null
        && per100G == null
        && perServing == null
        && (unit == null || unit.isEmpty())
      );
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Daily Value: %s%n", printable.get("dailyValue"))
        + String.format("  :Per 100g: %s%n", printable.get("per100G"))
        + String.format("  :Per Serving: %s%n", printable.get("perServing"))
        + String.format("  :Unit: %s%n", printable.get("unit"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Daily Value: %s", printable.get("dailyValue"))
      + String.format(", Per 100g: %s", printable.get("per100G"))
      + String.format(", Per Serving: %s", printable.get("perServing"))
      + String.format(", Unit: %s", printable.get("unit"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put(
        "dailyValue",
        SummaryHelper.formatAmount(this.dailyValue)
    );
    printable.put(
        "per100G",
        SummaryHelper.formatAmount(this.per100G)
    );
    printable.put(
        "perServing",
        SummaryHelper.formatAmount(this.perServing)
    );
    printable.put("unit", SummaryHelper.formatForDisplay(this.unit, null));
    return printable;
  }
}
