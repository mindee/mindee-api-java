package com.mindee.parsing.generated;

import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonUtils;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * An object within a feature.
 */
public class GeneratedObject extends HashMap<String, Object> {

  /**
   * Represent the object as a standard {@link StringField}.
   */
  public StringField asStringField() {
    return new StringField(
      (String) this.get("value"),
      (String) this.get("raw_value"),
      this.getConfidence(),
      this.getAsPolygon("polygon"),
      this.getPageId()
    );
  }

  /**
   * Represent the object as a standard {@link AmountField}.
   */
  public AmountField asAmountField() {
    return new AmountField(
      (Double) this.get("value"),
      this.getConfidence(),
      this.getAsPolygon("polygon"),
      this.getPageId()
    );
  }

  /**
   * Represent the object as a standard {@link DateField}.
   */
  public DateField asDateField() {
    return new DateField(
      LocalDate.parse((String) this.get("value")),
      this.getConfidence(),
      this.getAsPolygon("polygon"),
      this.getPageId()
    );
  }

  /**
   * Represent the object as a standard {@link ClassificationField}.
   */
  public ClassificationField asClassificationField() {
    return new ClassificationField(
      (String) this.get("value")
    );
  }

  public Polygon getAsPolygon(String key) {
    return PolygonUtils.getFrom((List<List<Double>>) this.get(key));
  }

  public Integer getPageId() {
    return (Integer) this.get("page_id");
  }

  public Double getConfidence() {
    return (Double) this.get("confidence");
  }
}
