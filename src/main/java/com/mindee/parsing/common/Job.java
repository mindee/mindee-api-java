package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Jon Details for enqued jobs
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
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
