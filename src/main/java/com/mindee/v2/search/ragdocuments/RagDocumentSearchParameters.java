package com.mindee.v2.search.ragdocuments;

import com.mindee.v2.clientoptions.BaseSearchParameters;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Search parameters for RAG Documents.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class RagDocumentSearchParameters extends BaseSearchParameters<RagDocumentSearchResponse> {
  /**
   * Model identifier to search in.
   */
  private final String modelId;

  /**
   * Case-insensitive substring search on filename.
   */
  private final String filename;

  private RagDocumentSearchParameters(
      String modelId,
      String filename,
      Integer page,
      Integer perPage
  ) {
    super(RagDocumentSearchResponse.class, page, perPage);
    if (modelId == null || modelId.trim().isEmpty()) {
      throw new IllegalArgumentException("ModelId is required in RagDocumentSearchParameters");
    }
    this.modelId = modelId;
    this.filename = filename;
  }

  @Override
  public Map<String, String> getRequestParameters() {
    var parameters = new HashMap<>(super.getRequestParameters());

    parameters.put("model_id", this.getModelId());

    if (this.getFilename() != null && !this.getFilename().isEmpty()) {
      parameters.put("filename", this.getFilename());
    }

    return parameters;
  }

  /**
   * Create a new builder.
   *
   * @param modelId the mandatory model identifier
   * @return a fresh {@link Builder}
   */
  public static Builder builder(String modelId) {
    return new Builder(modelId);
  }

  /**
   * Fluent builder for {@link RagDocumentSearchParameters}.
   */
  public static final class Builder extends BaseSearchParameters.BaseBuilder<Builder> {
    private final String modelId;
    private String filename;

    Builder(String modelId) {
      this.modelId = modelId;
    }

    /**
     * Case-insensitive substring search on filename.
     */
    public Builder filename(String filename) {
      if (filename != null && !filename.isEmpty()) {
        this.filename = filename;
      }
      return this;
    }

    /**
     * Build an immutable {@link RagDocumentSearchParameters} instance.
     */
    public RagDocumentSearchParameters build() {
      return new RagDocumentSearchParameters(this.modelId, this.filename, this.page, this.perPage);
    }
  }
}
