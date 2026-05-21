package com.mindee.v2.parsing.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Model webhook info.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class ModelWebhook {
  /**
   * ID of the webhook.
   */
  @JsonProperty("id")
  private String id;

  /**
   * Name of the webhook.
   */
  @JsonProperty("name")
  private String name;

  /**
   * URL of the webhook.
   */
  @JsonProperty("url")
  private String url;

}
