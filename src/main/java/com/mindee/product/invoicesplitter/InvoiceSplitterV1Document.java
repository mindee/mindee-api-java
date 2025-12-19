package com.mindee.product.invoicesplitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Invoice Splitter API version 1.4 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceSplitterV1Document extends Prediction {

  /**
   * List of page groups. Each group represents a single invoice within a multi-invoice document.
   */
  @JsonProperty("invoice_page_groups")
  protected List<InvoiceSplitterV1InvoicePageGroup> invoicePageGroups = new ArrayList<>();

  @Override
  public boolean isEmpty() {
    return ((this.invoicePageGroups == null || this.invoicePageGroups.isEmpty()));
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    String invoicePageGroupsSummary = "";
    if (!this.getInvoicePageGroups().isEmpty()) {
      int[] invoicePageGroupsColSizes = new int[] { 74 };
      invoicePageGroupsSummary = String
        .format("%n%s%n  ", SummaryHelper.lineSeparator(invoicePageGroupsColSizes, "-"))
        + "| Page Indexes                                                             "
        + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(invoicePageGroupsColSizes, "="));
      invoicePageGroupsSummary += SummaryHelper
        .arrayToString(this.getInvoicePageGroups(), invoicePageGroupsColSizes);
      invoicePageGroupsSummary += String
        .format("%n%s", SummaryHelper.lineSeparator(invoicePageGroupsColSizes, "-"));
    }
    outStr.append(String.format(":Invoice Page Groups: %s%n", invoicePageGroupsSummary));
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
