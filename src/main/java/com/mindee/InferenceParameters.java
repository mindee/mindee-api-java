package com.mindee;

import com.mindee.input.PageOptions;
import java.util.Collections;
import java.util.List;
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
   * ID of the model (required).
   */
  private final String modelId;
  /**
   * Enables Retrieval-Augmented Generation (optional, default: {@code false}).
   */
  private final boolean rag;
  /**
   * Optional alias for the file.
   */
  private final String alias;
  /**
   * IDs of webhooks to propagate the API response to (may be empty).
   */
  private final List<String> webhookIds;
  /*
   * Asynchronous polling options.
   */
  private final AsyncPollingOptions pollingOptions;

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
    private boolean rag = false;
    private String alias;
    private List<String> webhookIds = Collections.emptyList();
    private AsyncPollingOptions pollingOptions = AsyncPollingOptions.builder().build();

    private Builder(String modelId) {
      this.modelId = Objects.requireNonNull(modelId, "modelId must not be null");
    }

    /** Enable / disable Retrieval-Augmented Generation. */
    public Builder rag(boolean rag) {
      this.rag = rag;
      return this;
    }

    /** Set an alias for the uploaded document. */
    public Builder alias(String alias) {
      this.alias = alias;
      return this;
    }

    /** Provide IDs of webhooks to forward the API response to. */
    public Builder webhookIds(List<String> webhookIds) {
      this.webhookIds = webhookIds;
      return this;
    }


    public Builder pollingOptions(AsyncPollingOptions pollingOptions) {
      this.pollingOptions = pollingOptions;
      return this;
    }

    /** Build an immutable {@link InferenceParameters} instance. */
    public InferenceParameters build() {
      return new InferenceParameters(
          modelId,
          rag,
          alias,
          webhookIds,
          pollingOptions
      );
    }
  }
}
