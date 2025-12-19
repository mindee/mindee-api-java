package com.mindee.product.billoflading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The party to whom the goods are being shipped.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillOfLadingV1Consignee extends BaseField {

  /**
   * The address of the consignee.
   */
  @JsonProperty("address")
  String address;
  /**
   * The email of the shipper.
   */
  @JsonProperty("email")
  String email;
  /**
   * The name of the consignee.
   */
  @JsonProperty("name")
  String name;
  /**
   * The phone number of the consignee.
   */
  @JsonProperty("phone")
  String phone;

  public boolean isEmpty() {
    return ((address == null || address.isEmpty())
      && (email == null || email.isEmpty())
      && (name == null || name.isEmpty())
      && (phone == null || phone.isEmpty()));
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Address: %s%n", printable.get("address"))
      + String.format("  :Email: %s%n", printable.get("email"))
      + String.format("  :Name: %s%n", printable.get("name"))
      + String.format("  :Phone: %s%n", printable.get("phone"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Address: %s", printable.get("address"))
      + String.format(", Email: %s", printable.get("email"))
      + String.format(", Name: %s", printable.get("name"))
      + String.format(", Phone: %s", printable.get("phone"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("address", SummaryHelper.formatForDisplay(this.address, null));
    printable.put("email", SummaryHelper.formatForDisplay(this.email, null));
    printable.put("name", SummaryHelper.formatForDisplay(this.name, null));
    printable.put("phone", SummaryHelper.formatForDisplay(this.phone, null));
    return printable;
  }
}
