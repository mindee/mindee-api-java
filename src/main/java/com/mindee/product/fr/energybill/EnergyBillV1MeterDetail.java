package com.mindee.product.fr.energybill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Information about the energy meter.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnergyBillV1MeterDetail extends BaseField {

  /**
   * The unique identifier of the energy meter.
   */
  @JsonProperty("meter_number")
  String meterNumber;
  /**
   * The type of energy meter.
   */
  @JsonProperty("meter_type")
  String meterType;
  /**
   * The unit of power for energy consumption.
   */
  @JsonProperty("unit")
  String unit;

  public boolean isEmpty() {
    return ((meterNumber == null || meterNumber.isEmpty())
      && (meterType == null || meterType.isEmpty())
      && (unit == null || unit.isEmpty()));
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Meter Number: %s%n", printable.get("meterNumber"))
      + String.format("  :Meter Type: %s%n", printable.get("meterType"))
      + String.format("  :Unit of Power: %s%n", printable.get("unit"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Meter Number: %s", printable.get("meterNumber"))
      + String.format(", Meter Type: %s", printable.get("meterType"))
      + String.format(", Unit of Power: %s", printable.get("unit"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("meterNumber", SummaryHelper.formatForDisplay(this.meterNumber, null));
    printable.put("meterType", SummaryHelper.formatForDisplay(this.meterType, null));
    printable.put("unit", SummaryHelper.formatForDisplay(this.unit, null));
    return printable;
  }
}
