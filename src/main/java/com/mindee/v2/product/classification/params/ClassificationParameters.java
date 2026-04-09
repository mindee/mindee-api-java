package com.mindee.v2.product.classification.params;

import com.mindee.v2.clientOptions.BaseParameters;
import com.mindee.v2.http.ProductInfo;

@ProductInfo(slug = "classification")
public class ClassificationParameters extends BaseParameters {
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

  public static final class Builder extends BaseParameters.BaseBuilder<Builder> {

    Builder(String modelId) {
      super(modelId);
    }

    /** Build an immutable {@link ClassificationParameters} instance. */
    public ClassificationParameters build() {
      return new ClassificationParameters(modelId, alias, webhookIds);
    }
  }
}
