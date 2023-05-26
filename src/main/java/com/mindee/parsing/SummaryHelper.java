package com.mindee.parsing;

import com.mindee.parsing.common.field.BaseField;
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

  /**
   * Given a list of fields, return a string.
   */
  public static <T extends BaseField> String arrayToString(List<T> list, String delimiter) {
    return list.stream()
      .map(T::toString)
      .collect(Collectors.joining(String.format(delimiter)));
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
