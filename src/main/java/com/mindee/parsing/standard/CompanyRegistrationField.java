package com.mindee.parsing.standard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Represent a company registration.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CompanyRegistrationField extends BaseField implements LineItemField {

  /**
   * The value of the field.
   */
  private final String value;

  /**
   * Type of the company registration number.
   */
  private final String type;

  public CompanyRegistrationField(
      @JsonProperty("type") String type,
      @JsonProperty("value") String value
  ) {
    this.type = type;
    this.value = value;
  }

  public boolean isEmpty() {
    return ((value == null || value.isEmpty()) && (type == null || type.isEmpty()));
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.printableValues();
    return String.format("| %-15s ", printable.get("type"))
      + String.format("| %-20s ", printable.get("value"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Type: %s", printable.get("type"))
      + String.format(", Value: %s", printable.get("value"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();
    printable.put("type", SummaryHelper.formatForDisplay(this.type, null));
    printable.put("value", SummaryHelper.formatForDisplay(this.value, null));
    return printable;
  }
}
