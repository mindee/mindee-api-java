package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * File info for V2 API.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class InferenceResultFile {
  /**
   * File name.
   */
  @JsonProperty("name")
  private String name;

  /**
   * Optional file alias.
   */
  @JsonProperty("alias")
  private String alias;

  public String toString() {
    return ":Name: " + name + "\n:Alias:" + (alias != null ? " " + alias : "");
  }
}
