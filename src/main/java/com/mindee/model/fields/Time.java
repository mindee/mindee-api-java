package com.mindee.model.fields;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.geometry.Polygon;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Time extends BaseField {

  private final LocalTime value;

  @Builder @Jacksonized
  public Time(Boolean reconstructed, Double confidence,
    Polygon polygon, @JsonProperty("page_id")Integer page, LocalTime value) {
    super(reconstructed,  confidence, polygon, page);
    this.value = value;
  }
}
