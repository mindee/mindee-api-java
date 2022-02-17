package com.mindee.model.fields;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
public class Field extends BaseField {

  private final String value;
  private final Map<String, String> extraFields;

  @Builder
  public Field(Boolean reconstructed, String rawValue, Double confidence,
      List<List<Double>> polygon, String value,
      @Singular Map<String, String> extraFields) {
    super(reconstructed, rawValue, confidence, polygon);
    this.value = value;
    this.extraFields = extraFields;
  }
}
