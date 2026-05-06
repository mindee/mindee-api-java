package com.mindee.v1.parsing;

import com.mindee.parsing.SummaryHelper;
import com.mindee.v1.parsing.standard.LineItemField;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Various static methods to help generate the prediction summaries.
 */
public final class SummaryHelperV1 extends SummaryHelper {
  public static <T extends LineItemField> String arrayToString(
      List<T> lineItems,
      int[] columnSizes
  ) {
    return lineItems
      .stream()
      .map(T::toTableLine)
      .collect(
        Collectors
          .joining(String.format("%n%s%n  ", SummaryHelperV1.lineSeparator(columnSizes, "-")))
      );
  }
}
