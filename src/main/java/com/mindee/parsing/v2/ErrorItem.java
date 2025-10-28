package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Error item model.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class ErrorItem {
  /**
   * A JSON Pointer to the location of the body property.
   */
  @JsonProperty("pointer")
  private String pointer;

  /**
   * Explicit information on the issue.
   */
  @JsonProperty("detail")
  private String detail;
}
