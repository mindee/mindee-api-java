package com.mindee.parsing.common.field;

import java.util.HashMap;

/**
 * Represent a single line in a field printable as an rST table.
 */
public abstract class LineItemField extends BaseField {
  public abstract String toTableLine();
  public abstract String toString();
  protected abstract HashMap<String, String> printableValues();
}
