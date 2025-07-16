package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Common response information from Mindee API V2.
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

  public void setRawResponse(String contents) {
    rawResponse = contents;
  }
}
