package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Generic result for any off-the-shelf Mindee V2 model.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class InferenceResult {

  /**
   * Model fields.
   */
  @JsonProperty("fields")
  private InferenceFields fields;

  /**
   * Options.
   */
  @JsonProperty("options")
  private InferenceOptions options;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    joiner.add(fields.toString());
    if (options != null) {
      joiner.add(options.toString());
    }
    return joiner.toString();
  }
}
