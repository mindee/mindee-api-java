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
   * Model result values.
   */
  @JsonProperty("result")
  private InferenceResult result;

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner("\n");
    sj.add("#########")
        .add("Inference")
        .add("#########")
        .add(":Model: " + (model != null ? model.getId() : ""))
        .add(":File:")
        .add("  :Name: " + (file != null ? file.getName() : ""))
        .add("  :Alias: " + (file != null ? file.getAlias() : ""))
        .add("")
        .add("Result")
        .add("======")
        .add(result != null ? result.toString() : "");
    return sj.toString().trim();
  }
}
