package com.mindee.parsing;

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
    if (amountValue == null) {
      return "";
    }
    DecimalFormat df = new DecimalFormat("0.00###");
    df.setMinimumFractionDigits(2);
    df.setMaximumFractionDigits(5);
    return df.format(amountValue);
  }

  public static String formatString(String str) {
    return str != null ? str : "";
  }

  /**
   * Given a list of fields, return a string.
   */
  public static <T> String arrayToString(List<T> list, String delimiter) {
    return list.stream().map(T::toString).collect(Collectors.joining(String.format(delimiter)));
  }

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

  /**
   * Truncates a string if it's too long for the requested width.
   */
  public static String formatForDisplay(String inputValue, Integer maxColSize) {
    if (inputValue == null || inputValue.isEmpty()) {
      return "";
    }
    String outputValue = inputValue.replace("\n", "\\n").replace("\t", "\\t").replace("\r", "\\r");
    if (maxColSize == null || outputValue.length() <= maxColSize) {
      return outputValue;
    } else {
      return outputValue.substring(0, maxColSize - 3) + "...";
    }
  }

  /**
   * Truncates a boolean string if it's too long for the requested width.
   */
  public static String formatForDisplay(Boolean inputValue, Integer maxColSize) {
    if (inputValue == null) {
      return "";
    }
    if (maxColSize == null || 3 <= maxColSize) {
      return inputValue ? "True" : "False";
    } else {
      return inputValue ? "Y" : "N";
    }
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
