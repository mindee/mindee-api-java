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
      this.get("raw_value") != null ? (String) this.get("raw_value") : null,
      this.getConfidence(),
      this.getPolygon(),
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
      this.getPolygon(),
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
      this.getPolygon(),
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

  /**
   * Get the polygon, if present.
   */
  public Polygon getPolygon() {
    return this.getAsPolygon("polygon");
  }

  /**
   * Get the specified key as a {@link Polygon} object.
   */
  public Polygon getAsPolygon(String key) {
    if (this.containsKey(key)) {
      return PolygonUtils.getFrom((List<List<Double>>) this.get(key));
    }
    return null;
  }

  /**
   * Get the page ID, if present.
   */
  public Integer getPageId() {
    return this.get("page_id") != null ? (Integer) this.get("page_id") : null;
  }

  /**
   * Get the confidence score, if present.
   */
  public Double getConfidence() {
    return this.get("confidence") != null ? (Double) this.get("confidence") : null;
  }
}
