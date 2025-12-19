package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.SummaryHelper;
import java.util.LinkedHashMap;
import java.util.StringJoiner;
import lombok.EqualsAndHashCode;

/**
 * Inference fields map.
 */
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InferenceFields extends LinkedHashMap<String, DynamicField> {

  /**
   * Retrieves the field as a `SimpleField`.
   *
   * @param fieldName the name of the field
   * @throws IllegalStateException if the field is not a SimpleField
   */
  public SimpleField getSimpleField(String fieldName) throws IllegalStateException {
    return this.get(fieldName).getSimpleField();
  }

  /**
   * Retrieves the field as a `ListField`.
   *
   * @param fieldName the name of the field
   * @throws IllegalStateException if the field is not a ListField
   */
  public ListField getListField(String fieldName) throws IllegalStateException {
    return this.get(fieldName).getListField();
  }

  /**
   * Retrieves the field as an `ObjectField`.
   *
   * @param fieldName the name of the field
   * @throws IllegalStateException if the field is not a ObjectField
   */
  public ObjectField getObjectField(String fieldName) throws IllegalStateException {
    return this.get(fieldName).getObjectField();
  }

  public String toString(int indent) {
    String padding = String.join("", java.util.Collections.nCopies(indent, "  "));
    if (this.isEmpty()) {
      return "";
    }
    StringJoiner joiner = new StringJoiner("\n");

    this.forEach((fieldKey, fieldInstance) -> {
      StringBuilder strBuilder = new StringBuilder();
      strBuilder.append(padding).append(":").append(fieldKey).append(": ");

      if (fieldInstance.getType() == DynamicField.FieldType.LIST_FIELD) {
        ListField listField = fieldInstance.getListField();
        if (listField.getItems() != null && !listField.getItems().isEmpty()) {
          strBuilder.append(listField);
        }
      } else if (fieldInstance.getType() == DynamicField.FieldType.OBJECT_FIELD) {
        strBuilder.append(fieldInstance.getObjectField());
      } else if (fieldInstance.getType() == DynamicField.FieldType.SIMPLE_FIELD) {
        strBuilder.append(fieldInstance.getSimpleField());
      }
      joiner.add(strBuilder);
    });
    return SummaryHelper.cleanSummary(joiner.toString());
  }

  @Override
  public String toString() {
    return this.toString(0);
  }
}
