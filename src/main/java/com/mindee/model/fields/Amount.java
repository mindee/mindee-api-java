package com.mindee.model.fields;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
public class Amount extends BaseField {

  private final Double value;

  @Builder
  public Amount(Boolean reconstructed, String rawValue, Double confidence,
      List<List<Double>> polygon, Double value) {
    super(reconstructed, rawValue, confidence, polygon);
    this.value = value;
  }


}
