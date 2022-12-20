package com.mindee.parsing.common.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class StringField extends BaseField {

  @JsonProperty("value")
  private String value;

  @Override
  public String toString() {
    return value == null ? "" : value;
  }
}
