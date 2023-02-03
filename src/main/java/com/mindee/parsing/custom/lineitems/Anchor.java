package com.mindee.parsing.custom.lineitems;

import lombok.Getter;

@Getter
public final class Anchor {
  private final String name;
  private final Double tolerance;

  /**
   * @param name
   * @param tolerance
   */
  public Anchor(String name, Double tolerance) {
    this.name = name;
    this.tolerance = tolerance;
  }

  /**
   * @param name
   */
  public Anchor(String name) {
    this.name = name;
    this.tolerance = 0.001d;
  }
}
