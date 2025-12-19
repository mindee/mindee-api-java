package com.mindee.product.billoflading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The shipping company responsible for transporting the goods.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillOfLadingV1Carrier extends BaseField {

  /**
   * The name of the carrier.
   */
  @JsonProperty("name")
  String name;
  /**
   * The professional number of the carrier.
   */
  @JsonProperty("professional_number")
  String professionalNumber;
  /**
   * The Standard Carrier Alpha Code (SCAC) of the carrier.
   */
  @JsonProperty("scac")
  String scac;

  public boolean isEmpty() {
    return ((name == null || name.isEmpty())
      && (professionalNumber == null || professionalNumber.isEmpty())
      && (scac == null || scac.isEmpty()));
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Name: %s%n", printable.get("name"))
      + String.format("  :Professional Number: %s%n", printable.get("professionalNumber"))
      + String.format("  :SCAC: %s%n", printable.get("scac"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Name: %s", printable.get("name"))
      + String.format(", Professional Number: %s", printable.get("professionalNumber"))
      + String.format(", SCAC: %s", printable.get("scac"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("name", SummaryHelper.formatForDisplay(this.name, null));
    printable
      .put("professionalNumber", SummaryHelper.formatForDisplay(this.professionalNumber, null));
    printable.put("scac", SummaryHelper.formatForDisplay(this.scac, null));
    return printable;
  }
}
