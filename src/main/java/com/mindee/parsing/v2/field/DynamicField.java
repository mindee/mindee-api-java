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

  public SimpleField getSimpleField() throws IllegalStateException {
    if (type != FieldType.SIMPLE_FIELD) {
      throw new IllegalStateException("Field is not a simple field");
    }
    return simpleField;
  }

  public ListField getListField() throws IllegalStateException {
    if (type != FieldType.LIST_FIELD) {
      throw new IllegalStateException("Field is not a list field");
    }
    return listField;
  }

  public ObjectField getObjectField() throws IllegalStateException {
    if (type != FieldType.OBJECT_FIELD) {
      throw new IllegalStateException("Field is not an object field");
    }
    return objectField;
  }

  /**
   * Returns the field as the specified class.
   *
   * @param type the class representing the desired field type
   * @param <T> the type of field to return
   * @throws IllegalArgumentException if the requested type is not SimpleField, ListField, or ObjectField
   * @throws IllegalStateException if the field's internal type does not match the requested type
   */
  public <T extends BaseField> T getField(Class<T> type) throws IllegalArgumentException {
    if (type == SimpleField.class) {
      return (T) this.getSimpleField();
    }
    if (type == ListField.class) {
      return (T) this.getListField();
    }
    if (type == ObjectField.class) {
      return (T) this.getObjectField();
    }
    throw new IllegalArgumentException(
        "Cannot cast to " + type.getSimpleName()
    );
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
