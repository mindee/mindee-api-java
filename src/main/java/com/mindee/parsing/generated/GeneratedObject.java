package com.mindee.parsing.generated;

import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonUtils;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.BooleanField;
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
   * @return A {@link StringField} containing a string value.
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
   * @return An {@link AmountField} containing a double value.
   */
  public AmountField asAmountField() {
    Double value;
    Object rawValue = this.get("value");
    if (rawValue instanceof Integer) {
      value = ((Integer) rawValue).doubleValue();
    }
    else if (rawValue instanceof Double) {
      value = (Double) rawValue;
    }
    else {
      throw new ClassCastException("Cannot cast " + rawValue + " to Double");
    }
    return new AmountField(
      value,
      this.getConfidence(),
      this.getPolygon(),
      this.getPageId()
    );
  }

  /**
   * Represent the object as a standard {@link BooleanField}.
   * @return A {@link BooleanField} containing a boolean value.
   */
  public BooleanField asBooleanField() {
    Object rawValue = this.get("value");
    if (rawValue instanceof Boolean) {
      return new BooleanField((Boolean)rawValue, this.getConfidence(), this.getPolygon(), this.getPageId());
    }
    throw new ClassCastException("Cannot cast " + rawValue + " to Boolean");
  }

  /**
   * Represent the object as a standard {@link DateField}.
   * @return A {@link DateField} containing a date value.
   */
  public DateField asDateField() {
    return new DateField(
      LocalDate.parse((String) this.get("value")),
      this.getConfidence(),
      this.getPolygon(),
      this.getPageId(),
      this.getIsComputed()
    );
  }

  /**
   * Represent the object as a standard {@link ClassificationField}.
   * @return A {@link ClassificationField} containing a string value.
   */
  public ClassificationField asClassificationField() {
    return new ClassificationField(
      (String) this.get("value")
    );
  }

  /**
   * Get the polygon, if present.
   * @return A {@link Polygon}.
   */
  public Polygon getPolygon() {
    return this.getAsPolygon("polygon");
  }

  /**
   * Get the specified key as a {@link Polygon} object.
   * @param key Key to retrieve the polygon from.
   * @return A {@link Polygon}.
   */
  public Polygon getAsPolygon(String key) {
    if (this.containsKey(key)) {
      return PolygonUtils.getFrom((List<List<Double>>) this.get(key));
    }
    return null;
  }

  /**
   * Get the page ID, if present.
   * @return A page ID, as an integer.
   */
  public Integer getPageId() {
    return this.get("page_id") != null ? (Integer) this.get("page_id") : null;
  }

  /**
   * Get the confidence score, if present.
   * @return The confidence score, as a double.
   */
  public Double getConfidence() {
    return this.get("confidence") != null ? (Double) this.get("confidence") : null;
  }

  /**
   * Get the information on whether the date field was extracted.
   * @return A boolean representation of the field.
   */
  public Boolean getIsComputed(){
    return (Boolean) this.get("is_computed");
  }
}
