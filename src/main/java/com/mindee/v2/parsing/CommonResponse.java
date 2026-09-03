package com.mindee.v2.parsing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base class for all responses from the V2 API.
 */
@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class CommonResponse {
  /**
   * The raw server response.
   * This is not formatted in any way by the library and may contain newline and tab characters.
   */
  private String rawResponse;
}
