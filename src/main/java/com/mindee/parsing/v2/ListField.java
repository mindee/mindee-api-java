package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Field holding a list of fields.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class ListField extends BaseField {

  /**
   * Items of the list.
   */
  @JsonProperty("items")
  private List<DynamicField> items = new ArrayList<>();

  @Override
  public String toString() {
    if (items == null || items.isEmpty()) {
      return "";
    }
    StringJoiner joiner = new StringJoiner("\n");
    items.forEach(f -> joiner.add(f == null ? "null" : f.toString()));
    return joiner.toString();
  }
}
