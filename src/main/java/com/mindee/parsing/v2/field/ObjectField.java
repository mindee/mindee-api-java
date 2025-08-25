package com.mindee.parsing.v2.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
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

  /**
   * Retrieves all sub-fields from the {@code fields} map as a {@link LinkedHashMap} of
   * {@code SimpleField} objects, keyed by their field names.
   *
   * @return a {@link LinkedHashMap} containing the field names as keys and their corresponding
   *         {@code SimpleField} instances as values
   * @throws IllegalStateException if any field is not of type {@code SIMPLE_FIELD}
   */
  public LinkedHashMap<String, SimpleField> getSimpleFields() throws IllegalStateException {
    LinkedHashMap<String, SimpleField> simpleFields = new LinkedHashMap<>();
    if (fields != null) {
      for (String fieldName : fields.keySet()) {
        simpleFields.put(fieldName, fields.getSimpleField(fieldName));
      }
    }
    return simpleFields;
  }

  @Override
  public String toString() {
    return "\n" + (fields != null ? fields.toString(1) : "");
  }

  public String toStringFromList() {
    return fields != null ? fields.toString(2).substring(4) : "";
  }
}
