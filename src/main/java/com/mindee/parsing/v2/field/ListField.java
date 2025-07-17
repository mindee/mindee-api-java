package com.mindee.parsing.v2.field;

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
      return "\n";
    }
    StringJoiner joiner = new StringJoiner("\n  * ");
    joiner.add("");
    for (DynamicField item : items) {
      if (item != null) {
        if (item.getType() == DynamicField.FieldType.OBJECT_FIELD) {
          joiner.add(item.getObjectField().toStringFromList());
        } else {
          joiner.add(item.toString());
        }
      }
    }
    return joiner.toString();
  }
}
