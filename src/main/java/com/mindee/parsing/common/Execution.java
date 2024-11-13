package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.product.generated.GeneratedV1Document;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Representation of a workflow execution.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class Execution<DocT extends Inference> implements ApiObject {
  /**
   * Identifier for the batch to which the execution belongs.
   */
  @JsonProperty("batch_name")
  private String batchName;

  /**
   * The time at which the execution started.
   */
  @JsonProperty("created_at")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime createdAt;

  /**
   * File representation within a workflow execution.
   */
  @JsonProperty("file")
  private ExecutionFile file;

  /**
   * Identifier for the execution.
   */
  @JsonProperty("id")
  private String id;

  /**
   * Deserialized inference object.
   */
  @JsonProperty("inference")
  private DocT inference;

  /**
   * Priority of the execution.
   */
  @JsonProperty("priority")
  private String priority;

  /**
   * The time at which the file was tagged as reviewed.
   */
  @JsonProperty("reviewed_at")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime reviewedAt;

  /**
   * The time at which the file was uploaded to a workflow.
   */
  @JsonProperty("available_at")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime availableAt;

  /**
   * Reviewed fields and values.
   */
  @JsonProperty("reviewed_prediction")
  private GeneratedV1Document reviewedPrediction;

  /**
   * Execution Status.
   */
  @JsonProperty("status")
  private String status;

  /**
   * Execution type.
   */
  @JsonProperty("type")
  private String type;

  /**
   * The time at which the file was uploaded to a workflow.
   */
  @JsonProperty("uploaded_at")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime uploadedAt;

  /**
   * Identifier for the workflow.
   */
  @JsonProperty("workflow_id")
  private String workflowId;
}
