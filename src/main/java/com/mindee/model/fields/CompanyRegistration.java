package com.mindee.model.fields;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.geometry.Polygon;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyRegistration extends BaseField {

  private final String value;
  private final String type;

  @Builder
  @Jacksonized
  public CompanyRegistration(Boolean reconstructed, Double confidence,
    Polygon polygon, @JsonProperty("page_id") Integer page, String value,
    String type) {
    super(reconstructed, confidence, polygon, page);
    this.value = value;
    this.type = type;
  }

}
