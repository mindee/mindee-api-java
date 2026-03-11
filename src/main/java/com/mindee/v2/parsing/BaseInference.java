package com.mindee.v2.parsing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.v2.*;
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
public abstract class BaseInference {
  /**
   * Inference ID.
   */
  @JsonProperty("id")
  protected String id;

  /**
   * Job the inference belongs to.
   */
  @JsonProperty("job")
  protected InferenceJob job;

  /**
   * Model info.
   */
  @JsonProperty("model")
  protected InferenceModel model;

  /**
   * File info.
   */
  @JsonProperty("file")
  protected InferenceFile file;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    joiner
      .add("Inference")
      .add("#########")
      .add(job.toString())
      .add("")
      .add(model.toString())
      .add("")
      .add(file.toString());
    return joiner.toString().trim() + "\n";
  }
}
