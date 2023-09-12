package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Base class for all responses from the Mindee API.
 */
@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
abstract public class ApiResponse {
  /**
   * Information from Mindee about the api request.
   */
  @JsonProperty("api_request")
  ApiRequest apiRequest;
  /**
   * The raw server response.
   * This is not formatted in any way by the library and may contain newline and tab characters.
   */
  private String rawResponse;

  public void setRawResponse(String contents) {
    rawResponse = contents;
  }
}
