package com.mindee.v2.product.crop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.v2.field.FieldLocation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Result of a cropped document region.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class CropItem {
  /**
   * Type or classification of the detected object.
   */
  @JsonProperty("object_type")
  private String objectType;
  /**
   * Location which includes cropping coordinates for the detected object, within the source
   * document.
   */
  @JsonProperty("location")
  private FieldLocation location;

  @Override
  public String toString() {
    return "* :Location: " + location + "\n  :Object Type: " + objectType;
  }
}
