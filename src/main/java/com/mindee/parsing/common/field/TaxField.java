package com.mindee.parsing.common.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import lombok.Getter;

/**
 * Represent a tax.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxField extends BaseField {

  /**
   * The total amount of the tax.
   */
  @JsonProperty("value")
  private Double value;
  /**
   * The tax code.
   */
  @JsonProperty("code")
  private String code;
  /**
   * The rate of the taxe.
   */
  @JsonProperty("rate")
  private Double rate;
  /**
   * The tax base.
   */
  @JsonProperty("base")
  private Double base;

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    if (value != null) {
      stringBuilder.append(SummaryHelper.formatAmount(this.value));
    }
    if (rate != null) {
      stringBuilder.append(" ");
      stringBuilder.append(SummaryHelper.formatAmount(rate));
      stringBuilder.append("%");
    }
    if (code != null) {
      stringBuilder.append(" ");
      stringBuilder.append(code);
    }
    if (base != null) {
      stringBuilder.append(" ");
      stringBuilder.append(base);
    }
    return stringBuilder.toString().trim();
  }
}
