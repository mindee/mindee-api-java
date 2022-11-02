package com.mindee.model.fields;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.model.geometry.Polygon;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tax extends BaseField {

  private final Double value;
  private final String code;
  private final Double rate;


  @Builder @Jacksonized
  public Tax(Boolean reconstructed, Double confidence,
    Polygon polygon, Integer page, Double value, String code, Double rate) {
    super(reconstructed, confidence, polygon, page);
    this.value = value;
    this.code = code;
    this.rate = rate;
  }

  public String taxSummary() {
    StringBuilder stringBuilder = new StringBuilder("");
    if (value != null) {
      stringBuilder.append(value);
    }
    if (rate != null) {
      stringBuilder.append(" ");
      stringBuilder.append(rate);
      stringBuilder.append("%");
    }
    if (code != null) {
      stringBuilder.append(" ");
      stringBuilder.append(code);
    }
    return stringBuilder.toString().trim();
  }
}
