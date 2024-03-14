package com.mindee.parsing.standard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonDeserializer;
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

  public AmountField(
      @JsonProperty("value")
      Double value,
      @JsonProperty("confidence")
      Double confidence,
      @JsonProperty("polygon")
      @JsonDeserialize(using = PolygonDeserializer.class)
      Polygon polygon,
      @JsonProperty("page_id")
      Integer pageId
  ) {
    super(confidence, polygon, pageId);
    this.value = value;
  }

  @Override
  public String toString() {
    return SummaryHelper.formatAmount(this.value);
  }
}
