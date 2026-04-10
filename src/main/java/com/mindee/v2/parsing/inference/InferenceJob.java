package com.mindee.v2.parsing.inference;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Information on the Job associated to a given Inference.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class InferenceJob {
  /**
   * UUID of the Job.
   */
  @JsonProperty("id")
  private String id;

  public String toString() {

    var joiner = new StringJoiner("\n");
    joiner.add("Job").add("===").add(":ID: " + id);
    return joiner.toString();
  }
}
