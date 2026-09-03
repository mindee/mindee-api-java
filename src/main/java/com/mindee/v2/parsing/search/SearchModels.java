package com.mindee.v2.parsing.search;

import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * List of search models.
 */
public class SearchModels extends ArrayList<SearchModel> {

  /**
   * Default string representation.
   */
  @Override
  public String toString() {
    if (this.isEmpty()) {
      return "\n";
    }
    var joiner = new StringJoiner("\n");
    for (SearchModel item : this) {
      joiner.add("* :Name: " + item.getName());
      joiner.add("  :ID: " + item.getId());
      joiner.add("  :Model Type: " + item.getModelType());
    }
    joiner.add("");
    return joiner.toString();
  }
}
