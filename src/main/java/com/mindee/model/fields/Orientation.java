package com.mindee.model.fields;


import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
public class Orientation extends BaseField {

  private final Integer value;

  @Builder
  public Orientation(Boolean reconstructed, String rawValue, Double confidence,
      List<List<Double>> polygon, Integer page, Integer value) {
    super(reconstructed, rawValue, confidence, polygon, page);
    this.value = value;
  }
}
