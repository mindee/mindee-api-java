package com.mindee;

import java.util.Objects;
import lombok.Data;
import lombok.Getter;

/**
 * Options to pass when calling methods using the API V2.
 */
@Getter
@Data
public final class InferenceParameters {
  /**
   * Model ID to use for the inference (required).
   */
  private final String modelId;
  /**
   * Enhance extraction accuracy with Retrieval-Augmented Generation.
   */
  private final Boolean rag;
  /**
   * Extract the full text content from the document as strings.
   */
  private final Boolean rawText;
  /**
   * Calculate bounding box polygons for all fields.
   */
  private final Boolean polygon;
  /**
   * Boost the precision and accuracy of all extractions.
   * Calculate confidence scores for all fields.
   */
  private final Boolean confidence;
  /**
   * Optional alias for the file.
   */
  private final String alias;
  /**
   * Webhook IDs to call after all processing is finished.
   * If empty, no webhooks will be used.
   */
  private final String[] webhookIds;
  /**
   * Polling options. Set only if having timeout issues.
   */
  private final AsyncPollingOptions pollingOptions;
  /**
   * Additional text context used by the model during inference.
   * Not recommended, for specific use only.
   */
  private final String textContext;
  /**
   * Dynamic changes to the data schema of the model for this inference.
   */
  private final String dataSchema;

  /**
   * Create a new builder.
   *
   * @param modelId the mandatory model identifier
   * @return a fresh {@link Builder}
   */
  public static Builder builder(String modelId) {
    return new Builder(modelId);
  }

  /**
   * Fluent builder for {@link InferenceParameters}.
   */
  public static final class Builder {

    private final String modelId;
    private Boolean rag = null;
    private Boolean rawText = null;
    private Boolean polygon = null;
    private Boolean confidence = null;
    private String alias;
    private String[] webhookIds = new String[] {};
    private String textContext;
    private String dataSchema;
    private AsyncPollingOptions pollingOptions = AsyncPollingOptions.builder().build();

    private Builder(String modelId) {
      this.modelId = Objects.requireNonNull(modelId, "modelId must not be null");
    }

    /** Enhance extraction accuracy with Retrieval-Augmented Generation. */
    public Builder rag(Boolean rag) {
      this.rag = rag;
      return this;
    }

    /** Extract the full text content from the document as strings. */
    public Builder rawText(Boolean rawText) {
      this.rawText = rawText;
      return this;
    }

    /** Calculate bounding box polygons for all fields. */
    public Builder polygon(Boolean polygon) {
      this.polygon = polygon;
      return this;
    }

    /**
     * Boost the precision and accuracy of all extractions.
     * Calculate confidence scores for all fields.
     */
    public Builder confidence(Boolean confidence) {
      this.confidence = confidence;
      return this;
    }

    /** Set an alias for the uploaded document. */
    public Builder alias(String alias) {
      this.alias = alias;
      return this;
    }

    /** Provide IDs of webhooks to forward the API response to. */
    public Builder webhookIds(String[] webhookIds) {
      this.webhookIds = webhookIds;
      return this;
    }

    /** Provide additional text context used by the model during inference. */
    public Builder textContext(String textContext) {
      this.textContext = textContext;
      return this;
    }

    /** Provide additional text context used by the model during inference. */
    public Builder dataSchema(String dataSchema) {
      this.dataSchema = dataSchema;
      return this;
    }

    /** Set polling options. */
    public Builder pollingOptions(AsyncPollingOptions pollingOptions) {
      this.pollingOptions = pollingOptions;
      return this;
    }

    /** Build an immutable {@link InferenceParameters} instance. */
    public InferenceParameters build() {
      return new InferenceParameters(
        modelId,
        rag,
        rawText,
        polygon,
        confidence,
        alias,
        webhookIds,
        pollingOptions,
        textContext,
        dataSchema
      );
    }
  }
}
