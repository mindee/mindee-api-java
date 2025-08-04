package com.mindee.parsing.v2.field;

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
    return "\n" + (fields != null ? fields.toString(1) : "");
  }

  public String toStringFromList() {
    return fields != null ? fields.toString(2).substring(4) : "";
  }
}
