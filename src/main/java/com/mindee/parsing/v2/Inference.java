package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Inference object for the V2 API.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class Inference {
  /**
   * Inference ID.
   */
  @JsonProperty("id")
  private String id;

  /**
   * Model info.
   */
  @JsonProperty("model")
  private InferenceModel model;

  /**
   * File info.
   */
  @JsonProperty("file")
  private InferenceFile file;

  /**
   * Active options for the inference.
   */
  @JsonProperty("active_options")
  private InferenceActiveOptions activeOptions;

  /**
   * Model result values.
   */
  @JsonProperty("result")
  private InferenceResult result;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    joiner
        .add("Inference")
        .add("#########")
        .add(model.toString())
        .add("")
        .add(file.toString())
        .add("")
        .add(activeOptions.toString())
        .add("")
        .add(result != null ? result.toString() : "");
    return joiner.toString().trim() + "\n";
  }
}
