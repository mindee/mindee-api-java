package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Field holding a single scalar value.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = SimpleFieldDeserializer.class)
public final class SimpleField extends BaseField {

  /**
   * Value (string, boolean, double, or {@code null}).
   */
  @JsonProperty("value")
  private Object value;

  public SimpleField(Object value, FieldConfidence confidence, List<FieldLocation> locations) {
    super(confidence, locations);
    this.value = value;
  }


  @Override
  public String toString() {
    if (value == null) return "";
    if (value.getClass().equals(Boolean.class)) {
      return ((Boolean) value) ? "True" : "False";
    }
    return value.toString();
  }
}
