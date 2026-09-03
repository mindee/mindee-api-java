package com.mindee.v2.product.ocr.params;

import com.mindee.v2.clientoptions.BaseProductParameters;
import com.mindee.v2.product.ProductAttributes;

@ProductAttributes(slug = "ocr")
public class OcrParameters extends BaseProductParameters {

  public OcrParameters(String modelId, String alias, String[] webhookIds) {
    super(modelId, alias, webhookIds);
  }

  /**
   * Create a new builder.
   *
   * @param modelId the mandatory model identifier
   * @return a fresh {@link OcrParameters.Builder}
   */
  public static Builder builder(String modelId) {
    return new Builder(modelId);
  }

  public static final class Builder extends BaseProductParameters.BaseBuilder<Builder> {

    Builder(String modelId) {
      super(modelId);
    }

    /** Build an immutable {@link OcrParameters} instance. */
    public OcrParameters build() {
      return new OcrParameters(modelId, alias, webhookIds);
    }
  }
}
