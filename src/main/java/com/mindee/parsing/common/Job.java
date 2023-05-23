package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Getter;

/**
 * Jon Details for enqued jobs
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {

  /*
   * The time at which the job finished
   */
  @JsonProperty("available_at")
  private LocalDateTime availableAt;

  /*
   * Identifier for the job
   */
  private String id;
  /*
   * The time at which the job started
   */
  @JsonProperty("issued_at")
  private LocalDateTime issuedAt;
  /*
   * Job Status
   */
  private String status;


}
