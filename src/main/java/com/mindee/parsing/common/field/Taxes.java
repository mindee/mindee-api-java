package com.mindee.parsing.common.field;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represent all the tax lines.
 */
public class Taxes extends ArrayList<TaxField> {
  protected String lineSeparator(String str) {
    return "  +"
      + String.format("%15s", "").replace(" ", str)
      + "+"
      + String.format("%8s", "").replace(" ", str)
      + "+"
      + String.format("%10s", "").replace(" ", str)
      + "+"
      + String.format("%15s", "").replace(" ", str)
      + "+";
  }

  /**
   * Default string representation.
   */
  public String toString() {
    if (this.isEmpty()) {
      return "";
    }
    return String.format("%n%s%n", this.lineSeparator("-"))
      + "  | Base          | Code   | Rate (%) | Amount        |"
      + String.format("%n%s%n  ", this.lineSeparator("="))
      + this.stream().map(TaxField::toTableLine).collect(Collectors.joining("%n  "))
      + String.format("%n%s", this.lineSeparator("-"));
  }
}
