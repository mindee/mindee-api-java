package com.mindee.parsing.custom.lineitems;

import lombok.Getter;

import java.util.List;

@Getter
public class Field {
  private String name;
  protected List<FieldValue> values;

  /**
   * @param name
   * @param values
   */
  public Field(
      String name,
      List<FieldValue> values) {
    this.name = name;
    this.values = values;
  }

}
