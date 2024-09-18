package com.mindee.product.nutritionfactslabel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The amount of nutrients in the product.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutritionFactsLabelV1Nutrient extends BaseField implements LineItemField {

  /**
   * DVs are the recommended amounts of nutrients to consume or not to exceed each day.
   */
  @JsonProperty("daily_value")
  Double dailyValue;
  /**
   * The name of nutrients of the product.
   */
  @JsonProperty("name")
  String name;
  /**
   * The amount of nutrients per 100g of the product.
   */
  @JsonProperty("per_100g")
  Double per100G;
  /**
   * The amount of nutrients per serving of the product.
   */
  @JsonProperty("per_serving")
  Double perServing;
  /**
   * The unit of measurement for the amount of nutrients.
   */
  @JsonProperty("unit")
  String unit;

  public boolean isEmpty() {
    return (
        dailyValue == null
        && (name == null || name.isEmpty())
        && per100G == null
        && perServing == null
        && (unit == null || unit.isEmpty())
      );
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.printableValues();
    return String.format("| %-11s ", printable.get("dailyValue"))
      + String.format("| %-20s ", printable.get("name"))
      + String.format("| %-8s ", printable.get("per100G"))
      + String.format("| %-11s ", printable.get("perServing"))
      + String.format("| %-4s |", printable.get("unit"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Daily Value: %s", printable.get("dailyValue"))
      + String.format(", Name: %s", printable.get("name"))
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
    printable.put("name", SummaryHelper.formatForDisplay(this.name, 20));
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
