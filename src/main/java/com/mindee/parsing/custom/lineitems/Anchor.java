package com.mindee.parsing.custom.lineitems;

import lombok.Getter;

/**
 * An anchor column.
 */
@Getter
public final class Anchor {
  private final String name;
  private final double tolerance;

  /**
   * Define an anchor with height tolerance.
   */
  public Anchor(String name, double tolerance) {
    this.name = name;
    this.tolerance = tolerance;
  }

  /**
   * Define an anchor with no tolerance.
   */
  public Anchor(String name) {
    this.name = name;
    this.tolerance = 0.01d;
  }
}
