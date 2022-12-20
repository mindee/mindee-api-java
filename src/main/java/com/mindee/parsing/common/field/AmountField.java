package com.mindee.parsing.common.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmountField extends BaseField {

  @JsonProperty("value")
  private Double value;

  @Override
  public String toString() {
    return SummaryHelper.formatAmount(this.value);
  }
}
