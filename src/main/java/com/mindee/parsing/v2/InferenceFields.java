package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.SummaryHelper;
import java.util.HashMap;
import java.util.StringJoiner;
import lombok.EqualsAndHashCode;

/**
 * Inference fields map.
 */
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InferenceFields extends HashMap<String, DynamicField> {
  @Override
  public String toString() {
    if (this.isEmpty()) {
      return "";
    }
    StringJoiner joiner = new StringJoiner("\n");

    this.forEach((fieldKey, fieldValue) -> {
      StringBuilder strBuilder = new StringBuilder();
      strBuilder.append(':').append(fieldKey).append(": ");

      if (fieldValue.getListField() != null) {
        ListField listField = fieldValue.getListField();
        if (listField.getItems() != null && !listField.getItems().isEmpty()) {
          strBuilder.append(listField);
        }
      } else if (fieldValue.getObjectField() != null) {
        strBuilder.append(fieldValue.getObjectField());
      } else if (fieldValue.getSimpleField() != null) {
        strBuilder.append(fieldValue.getSimpleField().getValue() != null ? fieldValue.getSimpleField().getValue() : "");
      }
      joiner.add(strBuilder);
    });

    return SummaryHelper.cleanSummary(joiner.toString());
  }
}
