package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.StringJoiner;
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
  private Map<String, DynamicField> fields;

  @Override
  public String toString() {
    if (fields == null || fields.isEmpty()) return "";
    StringJoiner joiner = new StringJoiner("\n");
    fields.forEach((k, v) -> joiner.add(k + ": " + v));
    return joiner.toString();
  }
}
