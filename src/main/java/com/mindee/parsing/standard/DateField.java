package com.mindee.parsing.standard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonDeserializer;
import java.time.LocalDate;
import lombok.Getter;

/**
 * Represent a date.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateField extends BaseField {

  /**
   * The value as {@link LocalDate} object.
   */
  @JsonProperty("value")
  private LocalDate value;

  /**
   * Whether the field was computed or retrieved directly from the document.
   */
  @JsonProperty("is_computed")
  private Boolean isComputed;

  public DateField(
      @JsonProperty("value")
      LocalDate value,
      @JsonProperty("confidence")
      Double confidence,
      @JsonProperty("polygon")
      @JsonDeserialize(using = PolygonDeserializer.class)
      Polygon polygon,
      @JsonProperty("page_id")
      Integer pageId,
      @JsonProperty("is_computed")
      Boolean isComputed
  ) {
    super(confidence, polygon, pageId);
    this.value = value;
    this.isComputed = isComputed;
  }

  public boolean isEmpty() {
    return this.value == null;
  }

  /**
   * The value of the field.
   */
  @Override
  public String toString() {
    return this.value == null ? "" : value.toString();
  }

}
