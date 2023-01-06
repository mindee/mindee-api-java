package com.mindee.parsing.customdocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
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
  private List<ListFieldValue> values = new ArrayList<>();

  /**
   * used to handle the particular case of classification type.
   * @param value the value of the document type.
   */
  @JsonSetter("value")
  private void setValueForClassification(String value) {
    values.add(new ListFieldValue(value));
  }

  @Override
  public String toString() {

    return String.format("%s",
      values.stream()
        .map(ListFieldValue::toString)
        .collect(Collectors.joining(" ")));
  }
}
