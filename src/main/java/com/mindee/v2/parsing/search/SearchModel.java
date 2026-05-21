package com.mindee.v2.parsing.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Models search response.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchModel {

  /**
   * ID of the model.
   */
  @JsonProperty("id")
  private String id;

  /**
   * Name of the model.
   */
  @JsonProperty("name")
  private String name;

  /**
   * Type of the model.
   */
  @JsonProperty("model_type")
  private String modelType;

  /**
   * Webhooks associated with the model.
   */
  @JsonProperty("webhooks")
  private List<ModelWebhook> webhooks;

  @Override
  public String toString() {
    var joiner = new StringJoiner("\n");
    joiner.add(":Name: " + name);
    joiner.add(":ID: " + id);
    joiner.add(":Model Type: " + modelType);
    return joiner.toString();
  }
}
