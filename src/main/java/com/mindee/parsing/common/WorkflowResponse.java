package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the server response after a document is sent to a workflow.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowResponse<DocT extends Inference> extends ApiResponse {
  /**
   * Set the prediction model used to parse the document.
   * The response object will be instantiated based on this parameter.
   */
  @JsonProperty("execution")
  Execution<DocT> execution;
}
