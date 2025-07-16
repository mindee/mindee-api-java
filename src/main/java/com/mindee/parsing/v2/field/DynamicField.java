package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Dynamically-typed field (simple / object / list).
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = DynamicFieldDeserializer.class)
@AllArgsConstructor
@NoArgsConstructor
public class DynamicField {

  /**
   * Type of the wrapped field.
   */
  private FieldType type;

  /**
   * Value as simple field.
   */
  private SimpleField simpleField;

  /**
   * Value as list field.
   */
  private ListField listField;

  /**
   * Value as object field.
   */
  private ObjectField objectField;

  public static DynamicField of(SimpleField value) {
    return new DynamicField(FieldType.SIMPLE_FIELD, value, null, null);
  }

  public static DynamicField of(ObjectField value) {
    return new DynamicField(FieldType.OBJECT_FIELD, null, null, value);
  }

  public static DynamicField of(ListField value) {
    return new DynamicField(FieldType.LIST_FIELD, null, value, null);
  }

  @Override
  public String toString() {
    if (simpleField != null) return simpleField.toString();
    if (listField   != null) return listField.toString();
    if (objectField != null) return objectField.toString();
    return "";
  }

  /**
   * Possible field kinds.
   */
  public enum FieldType { SIMPLE_FIELD, OBJECT_FIELD, LIST_FIELD }
}
