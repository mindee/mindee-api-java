package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.standard.PositionField;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Cropping result.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cropper {
  /*
   * List of positions within the image.
   */
  @JsonProperty("cropping")
  private List<PositionField> cropping;
}
