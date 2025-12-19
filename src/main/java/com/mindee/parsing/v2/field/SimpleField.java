package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
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

  /**
   * Retrieves the value of the field as a {@link String}.
   *
   * @return the field value as a string
   * @throws ClassCastException if the value cannot be cast to a string
   */
  public String getStringValue() throws ClassCastException {
    return (String) value;
  }

  /**
   * Retrieves the value of the field as a {@link Double}.
   *
   * @return the field value as a Double
   * @throws ClassCastException if the value cannot be cast to a Double
   */
  public Double getDoubleValue() throws ClassCastException {
    return (Double) value;
  }

  /**
   * Retrieves the value of the field as a {@link BigDecimal}.
   *
   * @return the field value as a BigDecimal
   * @throws ClassCastException if the value cannot be cast to a BigDecimal
   */
  public BigDecimal getBigDecimalValue() throws ClassCastException {
    if (value == null) {
      return null;
    }
    return BigDecimal.valueOf(getDoubleValue());
  }

  /**
   * Retrieves the value of the field as a {@link Boolean}.
   *
   * @return the field value as a Boolean
   * @throws ClassCastException if the value cannot be cast to a Boolean
   */
  public Boolean getBooleanValue() throws ClassCastException {
    return (Boolean) value;
  }

  @Override
  public String toString() {
    if (value == null)
      return "";
    if (value.getClass().equals(Boolean.class)) {
      return ((Boolean) value) ? "True" : "False";
    }
    return value.toString();
  }
}
