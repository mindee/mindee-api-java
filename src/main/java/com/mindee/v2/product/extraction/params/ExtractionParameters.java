package com.mindee.v2.product.extraction.params;

import com.mindee.v2.clientoptions.BaseParameters;
import com.mindee.v2.http.ProductInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;

/**
 * Options to pass when calling methods using the API V2.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ProductInfo(slug = "extraction")
public final class ExtractionParameters extends BaseParameters {
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

  public MultipartEntityBuilder buildHttpBody(MultipartEntityBuilder builder) {
    builder = super.buildHttpBody(builder);
    if (this.getRag() != null) {
      builder.addTextBody("rag", this.getRag().toString().toLowerCase());
    }
    if (this.getRawText() != null) {
      builder.addTextBody("raw_text", this.getRawText().toString().toLowerCase());
    }
    if (this.getPolygon() != null) {
      builder.addTextBody("polygon", this.getPolygon().toString().toLowerCase());
    }
    if (this.getConfidence() != null) {
      builder.addTextBody("confidence", this.getConfidence().toString().toLowerCase());
    }
    if (this.getTextContext() != null) {
      builder.addTextBody("text_context", this.getTextContext());
    }
    if (this.getDataSchema() != null) {
      builder.addTextBody("data_schema", this.getDataSchema());
    }
    return builder;
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
  public static final class Builder extends BaseParameters.BaseBuilder<Builder> {
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
