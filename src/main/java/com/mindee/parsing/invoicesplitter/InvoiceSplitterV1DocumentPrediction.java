package com.mindee.parsing.invoicesplitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Invoice Splitter, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceSplitterV1DocumentPrediction {

  @JsonProperty("invoice_page_groups")
  private List<PageIndexes> invoicePageGroups;

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();

    outStr.append(String.format(":Invoice Page Groups: %n"));
    if (invoicePageGroups != null) {
      String pageGroupsString = this.getInvoicePageGroups().stream()
          .map(PageIndexes::toString)
          .collect(Collectors.joining(String.format("%n")));
      outStr.append(String.format("%s%n", pageGroupsString));
    }
    return outStr.toString();
  }

  /**
   * Represents a grouping of pages.
   */
  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class PageIndexes {

    /**
     * The confidence about the zone of the value extracted. A value from 0 to 1.
     */
    @JsonProperty("confidence")
    private Double confidence;

    /**
     * The page indexes in the document that are grouped together
     */
    @JsonProperty("page_indexes")
    private List<Integer> pageIndexes;

    @Override
    public String toString() {
      return "  :Page indexes: ".concat(
          pageIndexes.stream().map((index) -> index.toString())
            .collect(Collectors.joining(", "))
      );
    }
  }
}
