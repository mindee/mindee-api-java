package com.mindee.v2.clientoptions;

import com.mindee.v2.parsing.BaseRagAnnotationResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;

/**
 * Base parameters for document upload operations.
 */
@Getter
public abstract class BaseRagDocumentUploadParameters<TAnnotationResponse extends BaseRagAnnotationResponse> {
  private final Class<TAnnotationResponse> responseClass;

  /**
   * UUID of the model that the uploaded RAG document is linked to.
   */
  private final String modelId;

  /**
   * Base constructor.
   *
   * @param modelId {@link #modelId}
   */
  protected BaseRagDocumentUploadParameters(
      Class<TAnnotationResponse> responseClass,
      String modelId
  ) {
    this.responseClass = Objects.requireNonNull(responseClass, "responseClass cannot be null");

    if (modelId == null || modelId.isBlank()) {
      throw new IllegalArgumentException("ModelId cannot be null or whitespace.");
    }
    this.modelId = modelId.trim();
  }

  /**
   * Gets the request parameters for the upload request.
   */
  public Map<String, String> getRequestParameters() {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("model_id", modelId);
    return parameters;
  }

  protected abstract static class BaseBuilder<T extends BaseBuilder<T>> {
    protected String modelId;

    @SuppressWarnings("unchecked")
    protected T self() {
      return (T) this;
    }

    protected BaseBuilder(String modelId) {
      this.modelId = modelId;
    }
  }
}
