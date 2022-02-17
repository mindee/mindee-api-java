package com.mindee.model.fields;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
public class Tax extends BaseField {

  private final Double value;
  private final String code;
  private final Double rate;

  @Builder
  public Tax(Boolean reconstructed, String rawValue, Double confidence,
      List<List<Double>> polygon, Double value, String code, Double rate) {
    super(reconstructed, rawValue, confidence, polygon);
    this.value = value;
    this.code = code;
    this.rate = rate;
  }
}
