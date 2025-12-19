package com.mindee.parsing.custom.lineitems;

import java.util.List;
import lombok.Getter;

/**
 * Line items details.
 */
@Getter
public class LineItems {
  private List<Line> rows;

  /**
   * Default constructor.
   * 
   * @param rows
   */
  public LineItems(List<Line> rows) {
    this.rows = rows;
  }

}
