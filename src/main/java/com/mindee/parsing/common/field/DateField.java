package com.mindee.parsing.common.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Getter;

/**
 * Represent a date.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateField extends BaseField {

  /**
   * The raw value of it.
   */
  @JsonProperty("value")
  private LocalDate value;

  /**
   * The value of the field.
   *
   * @return
   */
  @Override
  public String toString() {
    return this.value == null ? "" : value.toString();
  }

}
