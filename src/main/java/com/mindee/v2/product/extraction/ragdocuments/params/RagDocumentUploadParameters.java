package com.mindee.v2.product.extraction.ragdocuments.params;

import com.mindee.v2.clientoptions.BaseRagDocumentUploadParameters;
import com.mindee.v2.product.extraction.ragdocuments.ExtractionRagAnnotationResponse;

/**
 * Upload parameters for RAG documents.
 */
public class RagDocumentUploadParameters
    extends BaseRagDocumentUploadParameters<ExtractionRagAnnotationResponse> {

  /**
   * {@inheritDoc}
   *
   * @param modelId {@inheritDoc}
   */
  public RagDocumentUploadParameters(String modelId) {
    super(ExtractionRagAnnotationResponse.class, modelId);
  }

  /**
   * Create a new builder.
   *
   * @param modelId {@link BaseRagDocumentUploadParameters#getModelId()}
   * @return a fresh {@link Builder}
   */
  public static Builder builder(String modelId) {
    return new Builder(modelId);
  }

  /**
   * Fluent builder for {@link RagDocumentUploadParameters}.
   */
  public static final class Builder extends BaseRagDocumentUploadParameters.BaseBuilder<Builder> {

    Builder(String modelId) {
      super(modelId);
    }

    /** Build an immutable {@link RagDocumentUploadParameters} instance. */
    public RagDocumentUploadParameters build() {
      return new RagDocumentUploadParameters(modelId);
    }
  }
}
