package com.mindee.parsing.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
  @JsonProperty("values" )
  private List<ListFieldValue> values;

  public ListField() {
    this.values = new ArrayList<>();
  }

  public ListField(double confidence, List<ListFieldValue> values) {
    this.confidence = confidence;
    this.values = values;
  }

  @Override
  public String toString() {

    return String.format("%s",
      values.stream()
        .map(ListFieldValue::toString)
        .collect(Collectors.joining(" ")));
  }
}
