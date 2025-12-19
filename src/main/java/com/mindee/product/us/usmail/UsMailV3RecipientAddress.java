package com.mindee.product.us.usmail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The addresses of the recipients.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsMailV3RecipientAddress extends BaseField implements LineItemField {

  /**
   * The city of the recipient's address.
   */
  @JsonProperty("city")
  String city;
  /**
   * The complete address of the recipient.
   */
  @JsonProperty("complete")
  String complete;
  /**
   * Indicates if the recipient's address is a change of address.
   */
  @JsonProperty("is_address_change")
  Boolean isAddressChange;
  /**
   * The postal code of the recipient's address.
   */
  @JsonProperty("postal_code")
  String postalCode;
  /**
   * The private mailbox number of the recipient's address.
   */
  @JsonProperty("private_mailbox_number")
  String privateMailboxNumber;
  /**
   * Second part of the ISO 3166-2 code, consisting of two letters indicating the US State.
   */
  @JsonProperty("state")
  String state;
  /**
   * The street of the recipient's address.
   */
  @JsonProperty("street")
  String street;
  /**
   * The unit number of the recipient's address.
   */
  @JsonProperty("unit")
  String unit;

  public boolean isEmpty() {
    return ((city == null || city.isEmpty())
      && (complete == null || complete.isEmpty())
      && isAddressChange == null
      && (postalCode == null || postalCode.isEmpty())
      && (privateMailboxNumber == null || privateMailboxNumber.isEmpty())
      && (state == null || state.isEmpty())
      && (street == null || street.isEmpty())
      && (unit == null || unit.isEmpty()));
  }

  private Map<String, String> tablePrintableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("city", SummaryHelper.formatForDisplay(this.city, 15));
    printable.put("complete", SummaryHelper.formatForDisplay(this.complete, 35));
    printable.put("isAddressChange", SummaryHelper.formatForDisplay(this.isAddressChange, null));
    printable.put("postalCode", SummaryHelper.formatForDisplay(this.postalCode, null));
    printable
      .put("privateMailboxNumber", SummaryHelper.formatForDisplay(this.privateMailboxNumber, null));
    printable.put("state", SummaryHelper.formatForDisplay(this.state, null));
    printable.put("street", SummaryHelper.formatForDisplay(this.street, 25));
    printable.put("unit", SummaryHelper.formatForDisplay(this.unit, 15));
    return printable;
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.tablePrintableValues();
    return String.format("| %-15s ", printable.get("city"))
      + String.format("| %-35s ", printable.get("complete"))
      + String.format("| %-17s ", printable.get("isAddressChange"))
      + String.format("| %-11s ", printable.get("postalCode"))
      + String.format("| %-22s ", printable.get("privateMailboxNumber"))
      + String.format("| %-5s ", printable.get("state"))
      + String.format("| %-25s ", printable.get("street"))
      + String.format("| %-15s |", printable.get("unit"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("City: %s", printable.get("city"))
      + String.format(", Complete Address: %s", printable.get("complete"))
      + String.format(", Is Address Change: %s", printable.get("isAddressChange"))
      + String.format(", Postal Code: %s", printable.get("postalCode"))
      + String.format(", Private Mailbox Number: %s", printable.get("privateMailboxNumber"))
      + String.format(", State: %s", printable.get("state"))
      + String.format(", Street: %s", printable.get("street"))
      + String.format(", Unit: %s", printable.get("unit"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("city", SummaryHelper.formatForDisplay(this.city, null));
    printable.put("complete", SummaryHelper.formatForDisplay(this.complete, null));
    printable.put("isAddressChange", SummaryHelper.formatForDisplay(this.isAddressChange, null));
    printable.put("postalCode", SummaryHelper.formatForDisplay(this.postalCode, null));
    printable
      .put("privateMailboxNumber", SummaryHelper.formatForDisplay(this.privateMailboxNumber, null));
    printable.put("state", SummaryHelper.formatForDisplay(this.state, null));
    printable.put("street", SummaryHelper.formatForDisplay(this.street, null));
    printable.put("unit", SummaryHelper.formatForDisplay(this.unit, null));
    return printable;
  }
}
