package com.mindee.parsing.custom.lineitems;

import com.mindee.geometry.Bbox;
import com.mindee.parsing.custom.ListField;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Line {
  private final Integer rowNumber;
  private final Map<String, Field> fields;
  private Bbox bbox;
  private final Double heightTolerance;

  /**
   * @param rowNumber
   * @param fields
   */
  public Line(
      Integer rowNumber,
      Map<String, Field> fields) {
    this.rowNumber = rowNumber;
    this.fields = fields;
    this.heightTolerance = 0.0;
  }

  public Line(Integer rowNumber, Double heightTolerance) {
    this.rowNumber = rowNumber;
    this.fields = new HashMap<>();
    this.heightTolerance = heightTolerance;
  }

  /**
   * @param bbox the bbox to set
   */
  public void setBbox(Bbox bbox) {
    this.bbox = bbox;
  }

}
