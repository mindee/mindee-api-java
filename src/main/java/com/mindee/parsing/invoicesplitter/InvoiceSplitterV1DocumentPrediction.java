package com.mindee.parsing.invoicesplitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.PageIndexes;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceSplitterV1DocumentPrediction {

  @JsonProperty("invoice_page_groups")
  private List<PageIndexes> invoicePageGroups;

  @Override
  public String toString() {
    if (invoicePageGroups == null) {
      return "";
    }
    String summary = IntStream.range(0, invoicePageGroups.size())
        .mapToObj((index) -> String.format("Group %s ", index)
            .concat(invoicePageGroups.get(index).toString()))
        .collect(Collectors.joining("\n"));
    return SummaryHelper.cleanSummary(summary);
  }

}
