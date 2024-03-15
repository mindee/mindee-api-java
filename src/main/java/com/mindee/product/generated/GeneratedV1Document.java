package com.mindee.product.generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.generated.GeneratedFeature;
import com.mindee.parsing.generated.GeneratedV1PredictionDeserializer;
import java.util.Map;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for generated documents.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = GeneratedV1PredictionDeserializer.class)
public class GeneratedV1Document extends Prediction {
  /**
   * Hashmap containing the fields of the document.
   */
  private Map<String, GeneratedFeature> fields;

  @Override
  public boolean isEmpty() {
    return fields.isEmpty();
  }

  public String toString() {
    TreeMap<String, GeneratedFeature> sorted = new TreeMap<>(fields);
    StringBuilder summary = new StringBuilder();
    for (Map.Entry<String, GeneratedFeature> entry : sorted.entrySet()) {
      summary.append(String.format(":%s: %s%n", entry.getKey(), entry.getValue()));
    }
    return summary.toString();
  }
}
