package com.mindee.v2.product.extraction.params;

import com.mindee.v2.clientoptions.BaseProductParameters;
import com.mindee.v2.product.ProductAttributes;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Options to pass when calling methods using the API V2.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ProductAttributes(slug = "extraction")
public final class ExtractionParameters extends BaseProductParameters {
  /**
   * Enhance extraction accuracy with Retrieval-Augmented Generation.
   */
  private final Boolean rag;
  /**
   * Extract the full text content from the document as strings.
   */
  private final Boolean rawText;
  /**
   * Calculate bounding box polygons for all fields.
   */
  private final Boolean polygon;
  /**
   * Boost the precision and accuracy of all extractions.
   * Calculate confidence scores for all fields.
   */
  private final Boolean confidence;
  /**
   * Additional text context used by the model during inference.
   * Not recommended, for specific use only.
   */
  private final String textContext;
  /**
   * Dynamic changes to the data schema of the model for this inference.
   */
  private final String dataSchema;

  private ExtractionParameters(
      String modelId,
      String alias,
      String[] webhookIds,
      Boolean rag,
      Boolean rawText,
      Boolean polygon,
      Boolean confidence,
      String textContext,
      String dataSchema
  ) {
    super(modelId, alias, webhookIds);
    this.rag = rag;
    this.rawText = rawText;
    this.polygon = polygon;
    this.confidence = confidence;
    this.textContext = textContext;
    this.dataSchema = dataSchema;
  }

  @Override
  public Map<String, String> getRequestParameters() {
    var parameters = new HashMap<>(super.getRequestParameters());

    if (this.getRag() != null) {
      parameters.put("rag", this.getRag().toString().toLowerCase());
    }
    if (this.getRawText() != null) {
      parameters.put("raw_text", this.getRawText().toString().toLowerCase());
    }
    if (this.getPolygon() != null) {
      parameters.put("polygon", this.getPolygon().toString().toLowerCase());
    }
    if (this.getConfidence() != null) {
      parameters.put("confidence", this.getConfidence().toString().toLowerCase());
    }
    if (this.getTextContext() != null) {
      parameters.put("text_context", this.getTextContext());
    }
    if (this.getDataSchema() != null) {
      parameters.put("data_schema", this.getDataSchema());
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
   * Fluent builder for {@link ExtractionParameters}.
   */
  public static final class Builder extends BaseProductParameters.BaseBuilder<Builder> {
    private Boolean rag = null;
    private Boolean rawText = null;
    private Boolean polygon = null;
    private Boolean confidence = null;
    private String textContext;
    private String dataSchema;

    Builder(String modelId) {
      super(modelId);
    }

    /** Enhance extraction accuracy with Retrieval-Augmented Generation. */
    public Builder rag(Boolean rag) {
      this.rag = rag;
      return this;
    }

    /** Extract the full text content from the document as strings. */
    public Builder rawText(Boolean rawText) {
      this.rawText = rawText;
      return this;
    }

    /** Calculate bounding box polygons for all fields. */
    public Builder polygon(Boolean polygon) {
      this.polygon = polygon;
      return this;
    }

    /**
     * Boost the precision and accuracy of all extractions.
     * Calculate confidence scores for all fields.
     */
    public Builder confidence(Boolean confidence) {
      this.confidence = confidence;
      return this;
    }

    /** Provide additional text context used by the model during inference. */
    public Builder textContext(String textContext) {
      this.textContext = textContext;
      return this;
    }

    /** Provide additional text context used by the model during inference. */
    public Builder dataSchema(String dataSchema) {
      this.dataSchema = dataSchema;
      return this;
    }

    /** Build an immutable {@link ExtractionParameters} instance. */
    public ExtractionParameters build() {
      return new ExtractionParameters(
        modelId,
        alias,
        webhookIds,
        rag,
        rawText,
        polygon,
        confidence,
        textContext,
        dataSchema
      );
    }
  }
}
