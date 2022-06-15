package com.mindee.model.fields;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Value;


@Value
public class Date extends BaseField {

  private final LocalDate value;

  @Builder
  public Date(Boolean reconstructed, String rawValue, Double confidence,
      List<List<Double>> polygon, LocalDate value) {
    super(reconstructed, rawValue, confidence, polygon);
    this.value = value;
  }
}
