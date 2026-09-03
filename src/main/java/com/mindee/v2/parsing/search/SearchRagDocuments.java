package com.mindee.v2.parsing.search;

import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * List of RAG documents.
 */
public class SearchRagDocuments extends ArrayList<SearchRagDocument> {

  /**
   * Default string representation.
   */
  @Override
  public String toString() {
    if (this.isEmpty()) {
      return "\n";
    }
    var joiner = new StringJoiner("\n");
    for (SearchRagDocument item : this) {
      joiner.add("* :ID: " + item.getId());
      joiner.add("  :Model ID: " + item.getModelId());
      joiner.add("  :Filename: " + item.getFilename());
      joiner.add("  :Created At: " + item.getCreatedAt());
      joiner.add("  :Total Matches: " + item.getTotalMatches());
      joiner.add("  :Last Match At: " + item.getLastMatchAt());
      joiner.add("  :Status: " + item.getStatus());
    }
    joiner.add("");
    return joiner.toString();
  }
}
