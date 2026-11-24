package com.mindee.v2.product.split.params;

import com.mindee.v2.clientoptions.BaseClassificationProductParameters;
import com.mindee.v2.product.ProductAttributes;

@ProductAttributes(slug = "split")
public class SplitParameters extends BaseClassificationProductParameters {

  public SplitParameters(String modelId, String alias, String[] webhookIds) {
    this(modelId, alias, webhookIds, null);
  }

  public SplitParameters(
      String modelId,
      String alias,
      String[] webhookIds,
      String extractionTextContext
  ) {
    super(modelId, alias, webhookIds, extractionTextContext);
  }

  /**
   * Create a new builder.
   *
   * @param modelId the mandatory model identifier
   * @return a fresh {@link SplitParameters.Builder}
   */
  public static Builder builder(String modelId) {
    return new Builder(modelId);
  }

  public static final class Builder
      extends BaseClassificationProductParameters.BaseBuilder<Builder> {

    Builder(String modelId) {
      super(modelId);
    }

    /** Build an immutable {@link SplitParameters} instance. */
    public SplitParameters build() {
      return new SplitParameters(modelId, alias, webhookIds, extractionTextContext);
    }
  }
}
