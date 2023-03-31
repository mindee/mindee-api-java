package com.mindee.parsing.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.SummaryHelper;
import java.util.Map;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = CustomV1DocumentPredictionDeserializer.class)
public class CustomV1DocumentPrediction {

  /**
   * Classification fields.
   */
  private Map<String, ClassificationField> classificationFields;

  /**
   * Fields that are not classifications.
   */
  private Map<String, ListField> fields;

  @Override
  public String toString() {

    TreeMap<String, ListField> sorted = new TreeMap<>(fields);

    StringBuilder summary = new StringBuilder();

    for (Map.Entry<String, ClassificationField> entry : classificationFields.entrySet()) {
      summary.append(String.format(":%s: %s%n", entry.getKey(), entry.getValue()));
    }
    for (Map.Entry<String, ListField> entry : sorted.entrySet()) {
      summary.append(String.format(":%s: %s%n", entry.getKey(), entry.getValue()));
    }

    return SummaryHelper.cleanSummary(summary.toString());
  }
}
