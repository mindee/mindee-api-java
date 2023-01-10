package com.mindee.model.fields;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.geometry.Polygon;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;


@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Date extends BaseField {

  private final LocalDate value;

  @Builder @Jacksonized
  public Date(Boolean reconstructed, Double confidence,
    Polygon polygon, @JsonProperty("page_id") Integer page, LocalDate value) {
    super(reconstructed, confidence, polygon, page);
    this.value = value;
  }
}