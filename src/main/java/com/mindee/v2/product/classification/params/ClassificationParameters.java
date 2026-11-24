package com.mindee.v2.product.classification.params;

import com.mindee.v2.clientoptions.BaseClassificationProductParameters;
import com.mindee.v2.product.ProductAttributes;

@ProductAttributes(slug = "classification")
public class ClassificationParameters extends BaseClassificationProductParameters {
  public ClassificationParameters(String modelId, String alias, String[] webhookIds) {
    this(modelId, alias, webhookIds, null);
  }

  public ClassificationParameters(
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
   * @return a fresh {@link ClassificationParameters.Builder}
   */
  public static Builder builder(String modelId) {
    return new Builder(modelId);
  }

  public static final class Builder
      extends BaseClassificationProductParameters.BaseBuilder<Builder> {

    Builder(String modelId) {
      super(modelId);
    }

    /** Build an immutable {@link ClassificationParameters} instance. */
    public ClassificationParameters build() {
      return new ClassificationParameters(modelId, alias, webhookIds, extractionTextContext);
    }
  }
}
