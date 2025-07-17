package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.common.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Defines an enqueued Job.
 */

@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class Job {
  /**
   * Date and time the job was created at.
   */
  @JsonProperty("created_at")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;

  /**
   * ID of the job.
   */
  @JsonProperty("id")
  private String id;

  /**
   * Status of the job.
   */
  @JsonProperty("status")
  private String status;

  /**
   * Status of the job.
   */
  @JsonProperty("error")
  private ErrorResponse error;

  /**
   * ID of the model.
   */
  @JsonProperty("model_id")
  private String modelId;

  /**
   * Name of the file.
   */
  @JsonProperty("file_name")
  private String fileName;

  /**
   * Optional alias of the file.
   */
  @JsonProperty("file_alias")
  private String fileAlias;

  /**
   * Polling URL.
   */
  @JsonProperty("polling_url")
  private String pollingUrl;

  /**
   * Result URL, when available.
   */
  @JsonProperty("result_url")
  private String resultUrl;

  /**
   * Polling URL.
   */
  @JsonProperty("webhooks")
  private List<JobResponseWebhook> webhooks;
}
