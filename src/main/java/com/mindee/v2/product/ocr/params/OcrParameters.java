package com.mindee.v2.product.ocr.params;

import com.mindee.v2.clientOptions.BaseParameters;
import com.mindee.v2.http.ProductInfo;

@ProductInfo(slug = "ocr")
public class OcrParameters extends BaseParameters {

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

  public static final class Builder extends BaseParameters.BaseBuilder<Builder> {

    Builder(String modelId) {
      super(modelId);
    }

    /** Build an immutable {@link OcrParameters} instance. */
    public OcrParameters build() {
      return new OcrParameters(modelId, alias, webhookIds);
    }
  }
}
