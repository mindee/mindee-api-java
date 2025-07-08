package com.mindee.parsing.v2;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents an asynchronous polling response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class JobResponse extends CommonResponse {
  /**
   * Representation of the Job.
   */
  @JsonProperty("job")
  Job job;
}
