package com.mindee;

import com.mindee.parsing.common.ExecutionPriority;
import lombok.Builder;
import lombok.Value;

/**
 * Workflow-specific options.
 */
@Value
public class WorkflowOptions {
  /**
   * Alias to give to the file.
   */
  String alias;

  /**
   * Priority to give to the execution.
   */
  ExecutionPriority priority;
  /**
   * Whether to include the full text data for async APIs.
   * This performs a full OCR operation on the server and will increase response time and payload
   * size.
   */
  Boolean fullText;

  @Builder
  private WorkflowOptions(
      String alias,
      ExecutionPriority priority,
      Boolean fullText
  ) {
    this.alias = alias;
    this.priority = priority;
    this.fullText = fullText == null ? Boolean.FALSE : fullText;
  }
}
