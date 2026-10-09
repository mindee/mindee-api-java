package com.mindee.v2.clientoptions;

import com.mindee.v2.parsing.BaseRagAnnotationResponse;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;

/**
 * Base parameters for document annotations.
 */
@Getter
public abstract class BaseAnnotationParameters<TAnnotationResponse extends BaseRagAnnotationResponse> {
  private final Class<TAnnotationResponse> responseClass;

  /**
   * UUID of the annotated document.
   */
  private final String documentId;

  /**
   * Base constructor.
   *
   * @param documentId {@link #documentId}
   */
  protected BaseAnnotationParameters(Class<TAnnotationResponse> responseClass, String documentId) {
    this.responseClass = Objects.requireNonNull(responseClass, "responseClass cannot be null");

    if (documentId == null || documentId.trim().isEmpty()) {
      throw new IllegalArgumentException("DocumentId cannot be null or whitespace.");
    }

    // Note: DocumentId is included in the request URL path, it is not a parameter.
    this.documentId = documentId.trim();
  }

  /**
   * Gets the request parameters for the upload request.
   */
  public abstract Map<String, Object> getRequestParameters();

  protected abstract static class BaseBuilder<T extends BaseBuilder<T>> {
    protected String documentId;

    @SuppressWarnings("unchecked")
    protected T self() {
      return (T) this;
    }

    protected BaseBuilder(String documentId) {
      this.documentId = documentId;
    }
  }
}
