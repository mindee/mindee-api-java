package com.mindee.parsing.standard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonDeserializer;
import lombok.Getter;

/**
 * Represent a string field.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class StringField extends BaseField {

  /**
   * The value of the field.
   */
  private final String value;

  /**
   * The value as it appears on the document.
   */
  private final String rawValue;

  public StringField(
      String value,
      Double confidence,
      Polygon polygon
  ) {
    this(value, null, confidence, polygon, null);
  }

  public StringField(
      @JsonProperty("value")
      String value,
      @JsonProperty("raw_value")
      String rawValue,
      @JsonProperty("confidence")
      Double confidence,
      @JsonProperty("polygon")
      @JsonDeserialize(using = PolygonDeserializer.class)
      Polygon polygon,
      @JsonProperty("page_id")
      Integer pageId
  ) {
    super(confidence, polygon, pageId);
    this.value = value;
    this.rawValue = rawValue;
  }

  @Override
  public String toString() {
    return value == null ? "" : value;
  }
}
