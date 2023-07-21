package com.mindee.parsing;

import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Various static methods to help generate the prediction summaries.
 */
public final class SummaryHelper {
  private SummaryHelper() {
    throw new IllegalStateException("Utility class");
  }

  public static String cleanSummary(String summaryToClean) {
    return summaryToClean.replace(String.format(" %n"), String.format("%n"));
  }

  public static String formatAmount(Double amountValue) {
    return amountValue == null ? "" : new DecimalFormat("#.00#").format(amountValue);
  }

  public static String formatString(String str) {
    return str != null ? str : "";
  }

  /**
   * Given a list of fields, return a string.
   */
  public static <T> String arrayToString(List<T> list, String delimiter) {
    return list.stream()
      .map(T::toString)
      .collect(Collectors.joining(String.format(delimiter)));
  }

  public static <T extends LineItemField> String arrayToString(List<T> lineItems, int[] columnSizes) {
    return lineItems.stream()
      .map(T::toTableLine)
      .collect(Collectors.joining(
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(columnSizes, "-")))
      );
  }

  /**
   * Format an rST table line separator.
   */
  public static String lineSeparator(int[] columnSizes, String str) {
    StringBuilder outStr = new StringBuilder("  +");
    for (int size : columnSizes) {
      outStr.append(String.format("%" + size + "s+", "").replace(" ", str));
    }
    return outStr.toString();
  }
}
