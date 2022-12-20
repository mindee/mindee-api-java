package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * The orientation which was applied from the original page.
 */
public class Orientation {

  /**
   * Degrees of the rotation.
   */
  @JsonProperty("value")
  private Integer value;
}
