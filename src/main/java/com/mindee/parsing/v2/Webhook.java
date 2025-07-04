package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.common.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Webhook info.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class Webhook {

  /**
   * ID of the webhook.
   */
  @JsonProperty("id")
  private String id;

  /**
   * An error encountered while processing the webhook.
   */
  @JsonProperty("error")
  private ErrorResponse error;

  /**
   * Date and time the webhook was created at.
   */
  @JsonProperty("created_at")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;

  /**
   * Status of the webhook.
   */
  @JsonProperty("status")
  private String status;
}
