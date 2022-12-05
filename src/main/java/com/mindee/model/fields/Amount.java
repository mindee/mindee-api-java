package com.mindee.model.fields;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.geometry.Polygon;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Amount extends BaseField {

  private final Double value;

  @Builder @Jacksonized
  public Amount(Boolean reconstructed,  Double confidence,
      Polygon polygon, @JsonProperty("page_id")  Integer page, Double value) {
    super(reconstructed, confidence, polygon, page);
    this.value = value;
  }


}
