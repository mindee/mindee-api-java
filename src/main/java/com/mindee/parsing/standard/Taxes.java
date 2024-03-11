package com.mindee.parsing.standard;

import com.mindee.parsing.SummaryHelper;
import java.util.ArrayList;

/**
 * Represent all the tax lines.
 */
public class Taxes extends ArrayList<TaxField> {

  /**
   * Default string representation.
   */
  public String toString() {
    if (this.isEmpty()) {
      return "";
    }
    int[] columnSizes = new int[] {15, 8, 10, 15};
    return String.format("%n%s%n", SummaryHelper.lineSeparator(columnSizes, "-"))
      + "  | Base          | Code   | Rate (%) | Amount        |"
      + String.format("%n%s%n  ", SummaryHelper.lineSeparator(columnSizes, "="))
      + SummaryHelper.arrayToString(this, columnSizes)
      + String.format("%n%s", SummaryHelper.lineSeparator(columnSizes, "-"));
  }
}
