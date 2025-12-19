package com.mindee.product.nutritionfactslabel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The amount of calories in the product.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutritionFactsLabelV1Calorie extends BaseField {

  /**
   * DVs are the recommended amounts of calories to consume or not to exceed each day.
   */
  @JsonProperty("daily_value")
  Double dailyValue;
  /**
   * The amount of calories per 100g of the product.
   */
  @JsonProperty("per_100g")
  Double per100G;
  /**
   * The amount of calories per serving of the product.
   */
  @JsonProperty("per_serving")
  Double perServing;

  public boolean isEmpty() {
    return (dailyValue == null && per100G == null && perServing == null);
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Daily Value: %s%n", printable.get("dailyValue"))
      + String.format("  :Per 100g: %s%n", printable.get("per100G"))
      + String.format("  :Per Serving: %s%n", printable.get("perServing"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Daily Value: %s", printable.get("dailyValue"))
      + String.format(", Per 100g: %s", printable.get("per100G"))
      + String.format(", Per Serving: %s", printable.get("perServing"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("dailyValue", SummaryHelper.formatAmount(this.dailyValue));
    printable.put("per100G", SummaryHelper.formatAmount(this.per100G));
    printable.put("perServing", SummaryHelper.formatAmount(this.perServing));
    return printable;
  }
}
