package com.mindee.parsing.custom.lineitems;

import lombok.Getter;

import java.util.List;

@Getter
public class LineItems {
  private List<Line> rows;

  /**
   * @param rows
   */
  public LineItems(List<Line> rows) {
    this.rows = rows;
  }

}
