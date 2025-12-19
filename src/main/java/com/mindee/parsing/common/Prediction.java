package com.mindee.parsing.common;

/**
 * Base class for all predictions.
 */
public abstract class Prediction {
  /**
   * Returns <code>true</code> if there are no predictions values.
   * Accessing prediction values when this is <code>true</code> may result in a
   * {@link NullPointerException}
   */
  public abstract boolean isEmpty();
}
