package com.mindee.v2.product.crop.params;

import com.mindee.v2.clientoptions.BaseProductParameters;
import com.mindee.v2.product.ProductAttributes;

@ProductAttributes(slug = "crop")
public class CropParameters extends BaseProductParameters {

  public CropParameters(String modelId, String alias, String[] webhookIds) {
    super(modelId, alias, webhookIds);
  }

  /**
   * Create a new builder.
   *
   * @param modelId the mandatory model identifier
   * @return a fresh {@link CropParameters.Builder}
   */
  public static Builder builder(String modelId) {
    return new Builder(modelId);
  }

  public static final class Builder extends BaseProductParameters.BaseBuilder<Builder> {

    Builder(String modelId) {
      super(modelId);
    }

    /** Build an immutable {@link CropParameters} instance. */
    public CropParameters build() {
      return new CropParameters(modelId, alias, webhookIds);
    }
  }
}
