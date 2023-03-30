package com.mindee.parsing.custom.lineitems;

import java.util.List;
import lombok.Getter;


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
