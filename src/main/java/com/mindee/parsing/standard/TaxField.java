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
  @JsonProperty("value")
  private Double value;
  /**
   * The tax code.
   */
  @JsonProperty("code")
  private String code;
  /**
   * The tax rate in percentage.
   */
  @JsonProperty("rate")
  private Double rate;
  /**
   * The tax base.
   */
  @JsonProperty("base")
  private Double base;

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
