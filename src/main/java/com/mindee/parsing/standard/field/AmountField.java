package com.mindee.parsing.standard.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import lombok.Getter;

/**
 * Represent an amount.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmountField extends BaseField {

  /**
   * An amount value.
   */
  @JsonProperty("value")
  private Double value;

  @Override
  public String toString() {
    return SummaryHelper.formatAmount(this.value);
  }
}
