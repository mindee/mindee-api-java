package com.mindee.parsing.common.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateField extends BaseField {

  @JsonProperty("value")
  private LocalDate value;

  @Override
  public String toString() {
    return this.value == null ? "" : value.toString();
  }

}
