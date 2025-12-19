package com.mindee.parsing.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A field containing a list of values.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListField {

  /**
   * The confidence about the zone of the value extracted.
   * A value from 0 to 1.
   */
  @JsonProperty("confidence")
  private double confidence;
  /**
   * List of defined values available in a field.
   */
  @JsonProperty("values")
  private List<ListFieldValue> values;

  public ListField() {
    this.values = new ArrayList<>();
  }

  /**
   * Returns <code>true</code> if there are no values.
   * 
   * @return <code>true</code> if there are no values.
   */
  public boolean isEmpty() {
    return this.values.isEmpty();
  }

  /**
   * Get all the value contents.
   * 
   * @return all the values as a list of strings.
   */
  public List<String> getContentsList() {
    return values.stream().map(ListFieldValue::getContent).collect(Collectors.toList());
  }

  /**
   * Get all the joined value contents.
   * 
   * @param separator the separator to use between values.
   * @return all the values as a single string.
   */
  public String getContentsString(String separator) {
    return String
      .format(
        "%s",
        values.stream().map(ListFieldValue::toString).collect(Collectors.joining(separator))
      );
  }

  @Override
  public String toString() {
    return getContentsString(" ");
  }
}
