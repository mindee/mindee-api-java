package com.mindee.v2.clientoptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * Base parameters for sending a file to a Mindee V2 product.
 */
@Data
public abstract class BaseProductParameters {
  /**
   * Model ID to use for the inference. Required.
   */
  protected final String modelId;
  /**
   * Optional: a free-form string to tag the request with your own identifier.
   * For example, an internal document ID, reference number, or database key.
   * If set, it will be included in the job and result responses.
   */
  protected final String alias;
  /**
   * Webhook IDs to call after all processing is finished.
   * If empty, no webhooks will be used.
   */
  protected final String[] webhookIds;

  protected BaseProductParameters(String modelId, String alias, String[] webhookIds) {
    if (modelId == null || modelId.trim().isBlank()) {
      throw new IllegalArgumentException("modelId cannot be null or whitespace.");
    }
    if ("".equals(alias)) {
      throw new IllegalArgumentException("alias cannot be an empty string.");
    }
    if (
      webhookIds != null && Arrays.stream(webhookIds).anyMatch(id -> id == null || id.isBlank())
    ) {
      throw new IllegalArgumentException(
        "WebhookIds cannot contain null, empty, or whitespace values."
      );
    }

    this.modelId = modelId.trim();
    this.alias = alias;
    this.webhookIds = webhookIds != null ? webhookIds : new String[0];
  }

  public Map<String, String> getRequestParameters() {
    var parameters = new HashMap<String, String>();

    parameters.put("model_id", this.getModelId());

    if (this.getAlias() != null) {
      parameters.put("alias", getAlias());
    }
    if (this.getWebhookIds() != null && this.getWebhookIds().length > 0) {
      parameters.put("webhook_ids", String.join(",", this.getWebhookIds()));
    }

    return parameters;
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
      this.modelId = modelId;
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
