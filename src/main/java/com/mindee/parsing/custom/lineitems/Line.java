package com.mindee.parsing.custom.lineitems;

import com.mindee.geometry.Bbox;
import com.mindee.geometry.BoundingBoxUtils;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonUtils;
import com.mindee.parsing.custom.ListFieldValue;
import com.mindee.parsing.standard.StringField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Represent a single line.
 */
@Getter
public class Line {
  private final Integer rowNumber;
  private final Map<String, StringField> fields;
  private final Double heightTolerance;
  @Setter
  private Bbox bbox;

  public Line(Integer rowNumber, Map<String, StringField> fields) {
    this.rowNumber = rowNumber;
    this.fields = fields;
    this.heightTolerance = 0.0;
  }

  public Line(Integer rowNumber, Double heightTolerance) {
    this.rowNumber = rowNumber;
    this.fields = new HashMap<>();
    this.heightTolerance = heightTolerance;
  }

  public void addField(String name, ListFieldValue fieldValue) {
    if (fields.containsKey(name)) {
      StringField existingField = fields.get(name);

      Polygon mergedPolygon = PolygonUtils
        .combine(
          BoundingBoxUtils.createBoundingBoxFrom(existingField.getPolygon()),
          BoundingBoxUtils.createBoundingBoxFrom(fieldValue.getPolygon())
        );

      String content = existingField.getValue() == null
          ? fieldValue.getContent()
          : String.join(" ", existingField.getValue(), fieldValue.getContent());

      fields
        .replace(
          name,
          new StringField(
            content,
            existingField.getConfidence() * fieldValue.getConfidence(),
            mergedPolygon
          )
        );
    } else {
      fields
        .put(
          name,
          new StringField(
            fieldValue.getContent(),
            fieldValue.getConfidence(),
            fieldValue.getPolygon()
          )
        );
    }
  }
}
