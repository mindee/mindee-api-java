package com.mindee.v1.parsing.standard;

import com.mindee.v1.parsing.SummaryHelperV1;
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
    int[] columnSizes = new int[] { 15, 8, 10, 15 };
    return String.format("%n%s%n", SummaryHelperV1.lineSeparator(columnSizes, "-"))
      + "  | Base          | Code   | Rate (%) | Amount        |"
      + String.format("%n%s%n  ", SummaryHelperV1.lineSeparator(columnSizes, "="))
      + SummaryHelperV1.arrayToString(this, columnSizes)
      + String.format("%n%s", SummaryHelperV1.lineSeparator(columnSizes, "-"));
  }
}
