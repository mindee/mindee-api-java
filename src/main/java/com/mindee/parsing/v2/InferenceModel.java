package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Model information for a V2 API inference.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class InferenceModel {

  /**
   * The ID of the model.
   */
  @JsonProperty("id")
  private String id;

  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    return joiner.add("Model").add("=====").add(":ID: " + id).toString();
  }
}
