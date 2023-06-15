package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
}
