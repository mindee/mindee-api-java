package com.mindee.parsing.common.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import lombok.Getter;

/**
 * Represent a tax line.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxField extends BaseField {

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
    String[] printable = this.printableValues();
    return "| "
      + String.format("%-13s", printable[0])
      + " | "
      + String.format("%-6s", printable[1])
      + " | "
      + String.format("%-8s", printable[2])
      + " | "
      + String.format("%-13s", printable[3])
      + " |";
  }

  @Override
  public String toString() {
    String[] printable = this.printableValues();
    return "Base: "
      + printable[0]
      + ", Code: "
      + printable[1]
      + ", Rate (%): "
      + printable[2]
      + ", Amount: "
      + printable[3].trim();
  }

  protected String[] printableValues() {
    return new String[]{
      this.base != null ? SummaryHelper.formatAmount(this.base) : "",
      this.code != null ? this.code : "",
      this.rate != null ? SummaryHelper.formatAmount(this.rate) : "",
      this.value != null ? SummaryHelper.formatAmount(this.value) : "",
    };
  }
}
