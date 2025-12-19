package com.mindee.product.fr.energybill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The company that supplies the energy.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnergyBillV1EnergySupplier extends BaseField {

  /**
   * The address of the energy supplier.
   */
  @JsonProperty("address")
  String address;
  /**
   * The name of the energy supplier.
   */
  @JsonProperty("name")
  String name;

  public boolean isEmpty() {
    return ((address == null || address.isEmpty()) && (name == null || name.isEmpty()));
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Address: %s%n", printable.get("address"))
      + String.format("  :Name: %s%n", printable.get("name"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Address: %s", printable.get("address"))
      + String.format(", Name: %s", printable.get("name"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("address", SummaryHelper.formatForDisplay(this.address, null));
    printable.put("name", SummaryHelper.formatForDisplay(this.name, null));
    return printable;
  }
}
