package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;

/**
 * Inference fields map.
 */
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InferenceFields extends HashMap<String, DynamicField> {
  @Override
  public String toString() {
    StringBuilder strBuilder = new StringBuilder();
    for (Map.Entry<String, DynamicField> entry : this.entrySet()) {
      strBuilder.append(':').append(entry.getKey()).append(": ").append(entry.getValue());
    }
    return strBuilder.toString();
  }
}
