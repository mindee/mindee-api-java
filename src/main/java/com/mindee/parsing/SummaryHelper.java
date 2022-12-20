package com.mindee.parsing;

import java.text.DecimalFormat;

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
}
