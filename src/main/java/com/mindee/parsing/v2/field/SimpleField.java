package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Field holding a single scalar value.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = SimpleFieldDeserializer.class)
@AllArgsConstructor
@NoArgsConstructor
public final class SimpleField extends BaseField {

  /**
   * Value (string, boolean, number … or {@code null}).
   */
  @JsonProperty("value")
  private Object value;

  @Override
  public String toString() {
    if (value == null) return "";
    if (value.getClass().equals(Boolean.class)) {
      return ((Boolean) value) ? "True" : "False";
    }
    return value.toString();
  }
}
