package com.mindee.parsing.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.SummaryHelper;
import java.util.HashMap;
import java.util.TreeMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * Page data for custom documents, API version 1.x.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomV1PagePrediction extends HashMap<String, ListField> {

  @Override
  public String toString() {

    TreeMap<String, ListField> sorted = new TreeMap<>(this);

    StringBuilder summary = new StringBuilder();

    for (Entry<String, ListField> entry : sorted.entrySet()) {
      summary.append(String.format(":%s: %s%n", entry.getKey(), entry.getValue()));
    }

    return SummaryHelper.cleanSummary(summary.toString());
  }
}
