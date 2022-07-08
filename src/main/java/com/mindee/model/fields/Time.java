package com.mindee.model.fields;

import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
public class Time extends BaseField {

  private final LocalTime value;

  @Builder
  public Time(Boolean reconstructed, String rawValue, Double confidence,
      List<List<Double>> polygon, Integer page, LocalTime value) {
    super(reconstructed, rawValue, confidence, polygon, page);
    this.value = value;
  }
}
