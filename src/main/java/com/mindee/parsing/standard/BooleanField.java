package com.mindee.parsing.standard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonDeserializer;
import lombok.Getter;


/**
 * Represent a date.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BooleanField extends BaseField {

  /**
   * The value as a {@link Boolean}.
   */
  @JsonProperty("value")
  private Boolean value;

  public BooleanField(
      @JsonProperty("value")
      Boolean value,
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
