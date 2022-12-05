package com.mindee.model.fields;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.geometry.Polygon;
import java.util.Map;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Field extends BaseField {

  private final String value;
  private final Map<String, String> extraFields;

  @Builder @Jacksonized
  public Field(Boolean reconstructed, Double confidence,
    Polygon polygon, @JsonProperty("page_id") Integer page, String value,
    @Singular Map<String, String> extraFields) {
    super(reconstructed, confidence, polygon, page);
    this.value = value;
    this.extraFields = extraFields;
  }
}
