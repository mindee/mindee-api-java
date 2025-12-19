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
   * Information about an error that occurred during the API request.
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

  public String toString() {
    return String.format("###########%n")
      + String.format("API Request%n")
      + String.format("###########%n")
      + String.format(":Status: %s%n", this.status)
      + String.format(":Status Code: %s%n", this.statusCode)
      + String.format(":Error: %s%n", this.error);
  }
}
