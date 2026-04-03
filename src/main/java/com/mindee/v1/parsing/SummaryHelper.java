package com.mindee.v1.parsing;

import com.mindee.v1.parsing.standard.LineItemField;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Various static methods to help generate the prediction summaries.
 */
public final class SummaryHelper extends com.mindee.parsing.SummaryHelper {
  public static <T extends LineItemField> String arrayToString(
      List<T> lineItems,
      int[] columnSizes
  ) {
    return lineItems
      .stream()
      .map(T::toTableLine)
      .collect(
        Collectors.joining(String.format("%n%s%n  ", SummaryHelper.lineSeparator(columnSizes, "-")))
      );
  }
}
