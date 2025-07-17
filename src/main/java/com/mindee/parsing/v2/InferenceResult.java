package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.v2.field.InferenceFields;
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
  private InferenceResultOptions options;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    joiner.add("Fields")
          .add("======");
    joiner.add(fields.toString());
    if (this.getOptions() != null) {
      joiner.add("Options")
          .add("=======")
          .add(this.getOptions().toString());
    }
    return joiner.toString();
  }
}
