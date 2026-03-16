package com.mindee.v2.clientOptions;

import com.mindee.AsyncPollingOptions;
import java.util.Objects;
import lombok.Data;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;

@Data
public abstract class BaseParameters {
  /**
   * Model ID to use for the inference (required).
   */
  protected final String modelId;
  /**
   * Optional alias for the file.
   */
  protected final String alias;
  /**
   * Webhook IDs to call after all processing is finished.
   * If empty, no webhooks will be used.
   */
  protected final String[] webhookIds;
  /**
   * Polling options. Set only if having timeout issues.
   */
  protected final AsyncPollingOptions pollingOptions;

  public MultipartEntityBuilder buildHttpBody(MultipartEntityBuilder builder) {
    builder.addTextBody("model_id", this.getModelId());
    if (this.getAlias() != null) {
      builder.addTextBody("alias", this.getAlias());
    }
    if (this.getWebhookIds().length > 0) {
      builder.addTextBody("webhook_ids", String.join(",", this.getWebhookIds()));
    }
    return builder;
  }

  public void validatePollingOptions() {
    if (pollingOptions.getInitialDelaySec() < 1) {
      throw new IllegalArgumentException("Initial delay must be ≥ 1 s");
    }
    if (pollingOptions.getIntervalSec() < 1) {
      throw new IllegalArgumentException("Interval must be ≥ 1 s");
    }
    if (pollingOptions.getMaxRetries() < 2) {
      throw new IllegalArgumentException("Max retries must be ≥ 2");
    }
  }

  protected static abstract class BaseBuilder<T extends BaseBuilder<T>> {
    protected final String modelId;
    protected String alias;
    protected String[] webhookIds = new String[] {};
    protected AsyncPollingOptions pollingOptions = AsyncPollingOptions.builder().build();

    @SuppressWarnings("unchecked")
    protected T self() {
      return (T) this;
    }

    protected BaseBuilder(String modelId) {
      this.modelId = Objects.requireNonNull(modelId, "modelId must not be null");
    }

    /** Set an alias for the uploaded document. */
    public T alias(String alias) {
      this.alias = alias;
      return self();
    }

    /** Provide IDs of webhooks to forward the API response to. */
    public T webhookIds(String[] webhookIds) {
      this.webhookIds = webhookIds;
      return self();
    }

    /** Set polling options. */
    public T pollingOptions(AsyncPollingOptions pollingOptions) {
      this.pollingOptions = pollingOptions;
      return self();
    }
  }

}
