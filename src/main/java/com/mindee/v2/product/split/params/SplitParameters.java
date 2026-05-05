package com.mindee.v2.product.split.params;

import com.mindee.v2.clientoptions.BaseParameters;
import com.mindee.v2.http.ProductInfo;

@ProductInfo(slug = "split")
public class SplitParameters extends BaseParameters {

  public SplitParameters(String modelId, String alias, String[] webhookIds) {
    super(modelId, alias, webhookIds);
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

  public static final class Builder extends BaseParameters.BaseBuilder<Builder> {

    Builder(String modelId) {
      super(modelId);
    }

    /** Build an immutable {@link SplitParameters} instance. */
    public SplitParameters build() {
      return new SplitParameters(modelId, alias, webhookIds);
    }
  }
}
