package com.mindee.product.invoicesplitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * List of page groups. Each group represents a single invoice within a multi-invoice document.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceSplitterV1InvoicePageGroup extends BaseField implements LineItemField {

  /**
   * List of page indexes that belong to the same invoice (group).
   */
  @JsonProperty("page_indexes")
  List<Integer> pageIndexes;

  public boolean isEmpty() {
    return (
        pageIndexes == null
      );
  }

  private Map<String, String> tablePrintableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put(
        "pageIndexes",
        this.pageIndexes.stream()
          .map(String::valueOf)
          .collect(Collectors.joining(", "))
    );
    return printable;
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.tablePrintableValues();
    return String.format("| %-72s |", printable.get("pageIndexes"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Page Indexes: %s", printable.get("pageIndexes"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put(
        "pageIndexes",
        this.pageIndexes.stream()
          .map(String::valueOf)
          .collect(Collectors.joining(", "))
    );
    return printable;
  }
}
