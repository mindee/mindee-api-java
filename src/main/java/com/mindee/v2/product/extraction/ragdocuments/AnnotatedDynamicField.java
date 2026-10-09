package com.mindee.v2.product.extraction.ragdocuments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.v2.parsing.inference.field.FieldType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Return the field class dynamically.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = DynamicAnnotationFieldDeserializer.class)
public class AnnotatedDynamicField {

  /**
   * Value as a simple field.
   */
  private AnnotatedSimpleField simpleField;

  /**
   * Value as an object field.
   */
  private AnnotatedObjectField objectField;

  /**
   * Value as a list field.
   */
  private AnnotatedListField listField;

  /**
   * The type of field.
   */
  private FieldType type;

  /**
   * Constructor for a simple field.
   */
  public AnnotatedDynamicField(AnnotatedSimpleField field) {
    this.type = FieldType.SIMPLE_FIELD;
    this.simpleField = field;
    this.objectField = null;
    this.listField = null;
  }

  /**
   * Constructor for a list field.
   */
  public AnnotatedDynamicField(AnnotatedListField field) {
    this.type = FieldType.LIST_FIELD;
    this.simpleField = null;
    this.objectField = null;
    this.listField = field;
  }

  /**
   * Constructor for an object field.
   */
  public AnnotatedDynamicField(AnnotatedObjectField field) {
    this.type = FieldType.OBJECT_FIELD;
    this.simpleField = null;
    this.objectField = field;
    this.listField = null;
  }

  @JsonValue
  public AnnotatedBaseField getField() {
    if (simpleField != null) {
      return simpleField;
    }
    if (listField != null) {
      return listField;
    }
    return objectField;
  }
}
