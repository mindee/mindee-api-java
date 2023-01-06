package com.mindee.parsing.customdocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindee.parsing.SummaryHelper;
import lombok.Getter;

import java.util.HashMap;
import java.util.TreeMap;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomDocumentPrediction extends HashMap<String, ListField> {

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
