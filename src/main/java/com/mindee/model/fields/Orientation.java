package com.mindee.model.fields;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.geometry.Polygon;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Orientation extends BaseField {

  private final Integer value;

  @Builder @Jacksonized
  public Orientation(Boolean reconstructed, Double confidence,
    Polygon polygon, Integer page, @JsonProperty("degrees") Integer value) {
    super(reconstructed, confidence, polygon, page);
    this.value = value;
  }
}
