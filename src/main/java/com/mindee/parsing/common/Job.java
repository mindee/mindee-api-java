package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Job Details for enqueued jobs.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class Job {

  /**
   * The time at which the job finished.
   */
  @JsonProperty("available_at")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime availableAt;

  /**
   * Identifier for the job.
   */
  @JsonProperty("id")
  private String id;

  /**
   * The time at which the job started.
   */
  @JsonProperty("issued_at")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime issuedAt;

  /**
   * Job Status.
   */
  @JsonProperty("status")
  private String status;

  /**
   * Information about an error that occurred during the job processing.
   */
  @JsonProperty("error")
  private Error error;
}
