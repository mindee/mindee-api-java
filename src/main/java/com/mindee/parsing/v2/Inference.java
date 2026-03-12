package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.v2.parsing.BaseInference;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Inference object for the V2 API.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class Inference extends BaseInference<InferenceResult> {
  /**
   * Active options for the inference.
   */
  @JsonProperty("active_options")
  private InferenceActiveOptions activeOptions;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    joiner.add(toStringBase()).add(activeOptions.toString()).add("").add(result.toString());
    return joiner.toString().trim() + "\n";
  }
}
