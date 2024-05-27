package com.mindee.parsing.standard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;


/**
 * Represent a tax line.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxField extends BaseField implements LineItemField {

  /**
   * The tax amount.
   */
  private final Double value;

  /**
   * The tax code.
   */
  private final String code;

  /**
   * The tax rate in percentage.
   */
  private final Double rate;

  /**
   * The tax base.
   */
  private final Double base;

  public TaxField(
      @JsonProperty("value")
      Double value,
      @JsonProperty("code")
      String code,
      @JsonProperty("rate")
      Double rate,
      @JsonProperty("base")
      Double base
  ) {
    this.value = value;
    this.code = code;
    this.rate = rate;
    this.base = base;
  }

  public boolean isEmpty() {
    return (
        value == null
        && (code == null || code.isEmpty())
        && rate == null
        && base == null
      );
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.printableValues();
    return "| "
      + String.format("%-13s", printable.get("base"))
      + " | "
      + String.format("%-6s", printable.get("code"))
      + " | "
      + String.format("%-8s", printable.get("rate"))
      + " | "
      + String.format("%-13s", printable.get("value"))
      + " |";
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return "Base: "
      + printable.get("base")
      + ", Code: "
      + printable.get("code")
      + ", Rate (%): "
      + printable.get("rate")
      + ", Amount: "
      + printable.get("value").trim();
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();
    printable.put("base", this.base != null ? SummaryHelper.formatAmount(this.base) : "");
    printable.put("code", this.code != null ? this.code : "");
    printable.put("rate", this.rate != null ? SummaryHelper.formatAmount(this.rate) : "");
    printable.put("value", this.value != null ? SummaryHelper.formatAmount(this.value) : "");
    return printable;
  }
}
