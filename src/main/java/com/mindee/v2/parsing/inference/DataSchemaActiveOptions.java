package com.mindee.v2.parsing.inference;

import static com.mindee.parsing.SummaryHelper.formatForDisplay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.StringJoiner;
import lombok.EqualsAndHashCode;

/**
 * Data schema options activated during the inference.
 */
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSchemaActiveOptions {

  @JsonProperty("replace")
  private boolean replace;

  /**
   * Whether the data schema was replaced for the inference.
   */
  public boolean getReplace() {
    return replace;
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner("\n");
    return joiner
      .add("Data Schema")
      .add("-----------")
      .add(":Replace: " + formatForDisplay(replace, 5))
      .toString();
  }
}
