package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Field holding a map of sub-fields.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class ObjectField extends BaseField {

  /**
   * Sub-fields keyed by their name.
   */
  @JsonProperty("fields")
  private InferenceFields fields;

  @Override
  public String toString() {
    if (fields == null || fields.isEmpty()) {
      return "\n";
    }

    StringBuilder outStr = new StringBuilder();

    fields.forEach((fieldKey, fieldValue) -> {
      outStr.append("\n");
      outStr.append(':').append(fieldKey).append(": ");

      if (fieldValue.getListField() != null) {
        ListField listField = fieldValue.getListField();
        if (listField.getItems() != null && !listField.getItems().isEmpty()) {
          outStr.append(listField);
        }
      } else if (fieldValue.getObjectField() != null) {
        outStr.append(fieldValue.getObjectField());
      } else if (fieldValue.getSimpleField() != null) {
        outStr.append(fieldValue.getSimpleField().getValue() != null ? fieldValue.getSimpleField().getValue() : "");
      }
    });

    return outStr + "\n";
  }

}
