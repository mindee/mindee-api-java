package com.mindee.v2.product.crop.params;

import com.mindee.v2.clientoptions.BaseClassificationProductParameters;
import com.mindee.v2.product.ProductAttributes;

@ProductAttributes(slug = "crop")
public class CropParameters extends BaseClassificationProductParameters {

  public CropParameters(String modelId, String alias, String[] webhookIds) {
    this(modelId, alias, webhookIds, null);
  }

  public CropParameters(
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
   * @return a fresh {@link CropParameters.Builder}
   */
  public static Builder builder(String modelId) {
    return new Builder(modelId);
  }

  public static final class Builder
      extends BaseClassificationProductParameters.BaseBuilder<Builder> {

    Builder(String modelId) {
      super(modelId);
    }

    /** Build an immutable {@link CropParameters} instance. */
    public CropParameters build() {
      return new CropParameters(modelId, alias, webhookIds, extractionTextContext);
    }
  }
}
