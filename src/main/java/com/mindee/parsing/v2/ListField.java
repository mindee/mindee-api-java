package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
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
      return "\n";
    }
    StringBuilder strBuilder = new StringBuilder();
    items.forEach(f -> strBuilder.append(f == null ? "" : f.toString()));
    return strBuilder.toString();
  }
}
