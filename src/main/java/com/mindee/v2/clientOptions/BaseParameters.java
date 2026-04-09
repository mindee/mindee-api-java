package com.mindee.v2.clientOptions;

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

  protected static abstract class BaseBuilder<T extends BaseBuilder<T>> {
    protected final String modelId;
    protected String alias;
    protected String[] webhookIds = new String[] {};

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
  }

}
