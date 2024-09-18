package com.mindee.product.us.usmail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The address of the sender.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsMailV2SenderAddress extends BaseField {

  /**
   * The city of the sender's address.
   */
  @JsonProperty("city")
  String city;
  /**
   * The complete address of the sender.
   */
  @JsonProperty("complete")
  String complete;
  /**
   * The postal code of the sender's address.
   */
  @JsonProperty("postal_code")
  String postalCode;
  /**
   * Second part of the ISO 3166-2 code, consisting of two letters indicating the US State.
   */
  @JsonProperty("state")
  String state;
  /**
   * The street of the sender's address.
   */
  @JsonProperty("street")
  String street;

  public boolean isEmpty() {
    return (
        (city == null || city.isEmpty())
        && (complete == null || complete.isEmpty())
        && (postalCode == null || postalCode.isEmpty())
        && (state == null || state.isEmpty())
        && (street == null || street.isEmpty())
      );
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :City: %s%n", printable.get("city"))
        + String.format("  :Complete Address: %s%n", printable.get("complete"))
        + String.format("  :Postal Code: %s%n", printable.get("postalCode"))
        + String.format("  :State: %s%n", printable.get("state"))
        + String.format("  :Street: %s%n", printable.get("street"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("City: %s", printable.get("city"))
      + String.format(", Complete Address: %s", printable.get("complete"))
      + String.format(", Postal Code: %s", printable.get("postalCode"))
      + String.format(", State: %s", printable.get("state"))
      + String.format(", Street: %s", printable.get("street"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("city", SummaryHelper.formatForDisplay(this.city, null));
    printable.put("complete", SummaryHelper.formatForDisplay(this.complete, null));
    printable.put("postalCode", SummaryHelper.formatForDisplay(this.postalCode, null));
    printable.put("state", SummaryHelper.formatForDisplay(this.state, null));
    printable.put("street", SummaryHelper.formatForDisplay(this.street, null));
    return printable;
  }
}
