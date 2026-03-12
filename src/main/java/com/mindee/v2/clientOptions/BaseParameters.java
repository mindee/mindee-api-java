package com.mindee.v2.clientOptions;

import com.mindee.AsyncPollingOptions;
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
  /**
   * Slug of the product type.
   */
  private final String slug;

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
}
