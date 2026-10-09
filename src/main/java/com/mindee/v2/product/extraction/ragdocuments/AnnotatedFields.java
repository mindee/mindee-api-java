package com.mindee.v2.product.extraction.ragdocuments;

import java.util.LinkedHashMap;

/**
 * A dictionary of field names and their corresponding annotation.
 */
public class AnnotatedFields extends LinkedHashMap<String, AnnotatedDynamicField> {
  /**
   * Retrieves the field as an {@link AnnotatedSimpleField}.
   *
   * @param fieldName the name of the field
   * @throws IllegalStateException if the field is not a SimpleField
   */
  public AnnotatedSimpleField getSimpleField(String fieldName) throws IllegalStateException {
    return this.get(fieldName).getSimpleField();
  }

  /**
   * Retrieves the field as a {@link AnnotatedListField}.
   *
   * @param fieldName the name of the field
   * @throws IllegalStateException if the field is not a ListField
   */
  public AnnotatedListField getListField(String fieldName) throws IllegalStateException {
    return this.get(fieldName).getListField();
  }

  /**
   * Retrieves the field as an {@link AnnotatedObjectField}.
   *
   * @param fieldName the name of the field
   * @throws IllegalStateException if the field is not a ObjectField
   */
  public AnnotatedObjectField getObjectField(String fieldName) throws IllegalStateException {
    return this.get(fieldName).getObjectField();
  }
}
