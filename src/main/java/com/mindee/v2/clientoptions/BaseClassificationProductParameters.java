package com.mindee.v2.clientoptions;

import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Base parameters for classification-based products.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class BaseClassificationProductParameters extends BaseProductParameters {
  /**
   * Pass additional context to the extraction model chained to this utility inference.
   */
  protected final String extractionTextContext;

  protected BaseClassificationProductParameters(
      String modelId,
      String alias,
      String[] webhookIds,
      String extractionTextContext
  ) {
    super(modelId, alias, webhookIds);
    this.extractionTextContext = extractionTextContext;
  }

  @Override
  public Map<String, String> getRequestParameters() {
    var parameters = new HashMap<>(super.getRequestParameters());

    if (this.getExtractionTextContext() != null) {
      parameters.put("extraction_text_context", this.getExtractionTextContext());
    }

    return parameters;
  }

  protected static abstract class BaseBuilder<T extends BaseBuilder<T>>
      extends BaseProductParameters.BaseBuilder<T> {
    protected String extractionTextContext;

    protected BaseBuilder(String modelId) {
      super(modelId);
    }

    /** Pass additional context to the extraction model chained to this utility inference. */
    public T extractionTextContext(String extractionTextContext) {
      this.extractionTextContext = extractionTextContext;
      return self();
    }
  }
}
