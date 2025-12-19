package com.mindee.product.billoflading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The goods being shipped.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillOfLadingV1CarrierItem extends BaseField implements LineItemField {

  /**
   * A description of the item.
   */
  @JsonProperty("description")
  String description;
  /**
   * The gross weight of the item.
   */
  @JsonProperty("gross_weight")
  Double grossWeight;
  /**
   * The measurement of the item.
   */
  @JsonProperty("measurement")
  Double measurement;
  /**
   * The unit of measurement for the measurement.
   */
  @JsonProperty("measurement_unit")
  String measurementUnit;
  /**
   * The quantity of the item being shipped.
   */
  @JsonProperty("quantity")
  Double quantity;
  /**
   * The unit of measurement for weights.
   */
  @JsonProperty("weight_unit")
  String weightUnit;

  public boolean isEmpty() {
    return ((description == null || description.isEmpty())
      && grossWeight == null
      && measurement == null
      && (measurementUnit == null || measurementUnit.isEmpty())
      && quantity == null
      && (weightUnit == null || weightUnit.isEmpty()));
  }

  private Map<String, String> tablePrintableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("description", SummaryHelper.formatForDisplay(this.description, 36));
    printable.put("grossWeight", SummaryHelper.formatAmount(this.grossWeight));
    printable.put("measurement", SummaryHelper.formatAmount(this.measurement));
    printable.put("measurementUnit", SummaryHelper.formatForDisplay(this.measurementUnit, null));
    printable.put("quantity", SummaryHelper.formatAmount(this.quantity));
    printable.put("weightUnit", SummaryHelper.formatForDisplay(this.weightUnit, null));
    return printable;
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.tablePrintableValues();
    return String.format("| %-36s ", printable.get("description"))
      + String.format("| %-12s ", printable.get("grossWeight"))
      + String.format("| %-11s ", printable.get("measurement"))
      + String.format("| %-16s ", printable.get("measurementUnit"))
      + String.format("| %-8s ", printable.get("quantity"))
      + String.format("| %-11s |", printable.get("weightUnit"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Description: %s", printable.get("description"))
      + String.format(", Gross Weight: %s", printable.get("grossWeight"))
      + String.format(", Measurement: %s", printable.get("measurement"))
      + String.format(", Measurement Unit: %s", printable.get("measurementUnit"))
      + String.format(", Quantity: %s", printable.get("quantity"))
      + String.format(", Weight Unit: %s", printable.get("weightUnit"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("description", SummaryHelper.formatForDisplay(this.description, null));
    printable.put("grossWeight", SummaryHelper.formatAmount(this.grossWeight));
    printable.put("measurement", SummaryHelper.formatAmount(this.measurement));
    printable.put("measurementUnit", SummaryHelper.formatForDisplay(this.measurementUnit, null));
    printable.put("quantity", SummaryHelper.formatAmount(this.quantity));
    printable.put("weightUnit", SummaryHelper.formatForDisplay(this.weightUnit, null));
    return printable;
  }
}
