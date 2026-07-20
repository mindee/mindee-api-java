package com.mindee.v2.parsing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.v1.parsing.common.LocalDateTimeDeserializer;
import com.mindee.v2.parsing.error.ErrorResponse;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents an asynchronous polling response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FailedInferenceResponse extends CommonResponse {
  /**
   * UUID of the failed inference.
   */
  @JsonProperty("inference_id")
  String inferenceId;

  /**
   * UUID of the model used.
   */
  @JsonProperty("model_id")
  String modelId;

  /**
   * Name of the input file.
   */
  @JsonProperty("file_name")
  String fileName;

  /**
   * Alias sent for the file, if any
   */
  @JsonProperty("file_alias")
  String fileAlias;

  /**
   * Problem details for the failure, if available.
   */
  @JsonProperty("error")
  ErrorResponse error;

  /**
   * Date and time when the inference was started.
   */
  @JsonProperty("created_at")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  LocalDateTime createdAt;
}
