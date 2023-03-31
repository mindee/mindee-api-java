package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * Information from Mindee about the api request.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiRequest {

  /**
   * Represent an error information from the API response.
   */
  @JsonProperty("error")
  private Error error;

  @JsonProperty("resources")
  private ArrayList<String> resources;

  /**
   * The status of the request.
   */
  @JsonProperty("status")
  private String status;

  /**
   * Status code of the request.
   */
  @JsonProperty("status_code")
  private int statusCode;

  /**
   * The original url.
   */
  @JsonProperty("url")
  private String url;
}
