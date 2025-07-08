package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
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

  /** Model fields. */
  @JsonProperty("fields")
  private InferenceFields fields;

  /** Options. */
  @JsonProperty("options")
  private InferenceOptions options;

  @Override
  public String toString() {
    if (fields == null || fields.isEmpty()) {
      return "";
    }
    StringJoiner joiner = new StringJoiner("\n");
    for (Map.Entry<String, DynamicField> e : fields.entrySet()) {
      joiner.add(":" + e.getKey() + ": " + e.getValue());
    }
    return joiner.toString();
  }
}
