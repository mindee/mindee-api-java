package com.mindee.product.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.custom.ClassificationField;
import com.mindee.parsing.custom.CustomV1DocumentPredictionDeserializer;
import com.mindee.parsing.custom.ListField;
import java.util.Map;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * Document data for custom documents, API version 1.x.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
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
