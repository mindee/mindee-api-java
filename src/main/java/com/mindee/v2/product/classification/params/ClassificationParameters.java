package com.mindee.v2.product.classification.params;

import com.mindee.v2.clientoptions.BaseProductParameters;
import com.mindee.v2.product.ProductAttributes;

@ProductAttributes(slug = "classification")
public class ClassificationParameters extends BaseProductParameters {
  public ClassificationParameters(String modelId, String alias, String[] webhookIds) {
    super(modelId, alias, webhookIds);
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

  public static final class Builder extends BaseProductParameters.BaseBuilder<Builder> {

    Builder(String modelId) {
      super(modelId);
    }

    /** Build an immutable {@link ClassificationParameters} instance. */
    public ClassificationParameters build() {
      return new ClassificationParameters(modelId, alias, webhookIds);
    }
  }
}
