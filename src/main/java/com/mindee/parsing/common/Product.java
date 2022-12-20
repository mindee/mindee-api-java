package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * The product on which the document was parsed.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

  /**
   * Name of the product used.
   */
  @JsonProperty("name")
  private String name;

  /**
   * Version of the product used.
   */
  @JsonProperty("version")
  private String version;
}
