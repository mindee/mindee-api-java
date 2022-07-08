package com.mindee.model.fields;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
public class Locale extends BaseField {

  private final java.util.Locale value;
  private final String language;
  private final String country;
  private final String currency;

  @Builder
  public Locale(Boolean reconstructed, String rawValue, Double confidence,
      List<List<Double>> polygon, Integer page, java.util.Locale value, String language,
      String country,
      String currency) {
    super(reconstructed, rawValue, confidence, polygon, page);
    this.value = value;
    this.language = language;
    this.country = country;
    this.currency = currency;
  }
}
