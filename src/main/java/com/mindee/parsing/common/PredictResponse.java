package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represent a predict response from Mindee API.
 *
 * @param <T> Set the prediction model used to parse the document.
 *            The response object will be instantiated based on this parameter.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictResponse<T extends Inference> {

  /**
   * Information from Mindee about the api request.
   */
  @JsonProperty("api_request")
  ApiRequest apiRequest;

  /**
   * Set the prediction model used to parse the document.
   * The response object will be instantiated based on this parameter.
   */
  @JsonProperty("document")
  Document<T> document;
}
