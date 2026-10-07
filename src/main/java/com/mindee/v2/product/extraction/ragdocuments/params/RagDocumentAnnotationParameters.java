package com.mindee.v2.product.extraction.ragdocuments.params;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mindee.exceptions.MindeeInputException;
import com.mindee.v2.clientoptions.BaseAnnotationParameters;
import com.mindee.v2.product.extraction.ragdocuments.ExtractionRagAnnotationResponse;
import com.mindee.v2.product.extraction.ragdocuments.RagAnnotation;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Annotation parameters for RAG documents.
 */
@Getter
public class RagDocumentAnnotationParameters
    extends BaseAnnotationParameters<ExtractionRagAnnotationResponse> {

  private static final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();

  /**
   * New public status to apply to the document (for example, to deactivate it).
   */
  private final String status;

  /**
   * Field-level RAG annotation and guidelines configuration for the document.
   */
  private final RagAnnotation annotation;

  /**
   * Constructor with only document ID.
   *
   * @param documentId {@link BaseAnnotationParameters#getDocumentId()}
   */
  public RagDocumentAnnotationParameters(String documentId) {
    this(documentId, null, null);
  }

  /**
   * Constructor with document ID and status.
   *
   * @param documentId {@link BaseAnnotationParameters#getDocumentId()}
   * @param status {@link #status}
   */
  public RagDocumentAnnotationParameters(String documentId, String status) {
    this(documentId, status, null);
  }

  /**
   * Default constructor.
   *
   * @param documentId {@link BaseAnnotationParameters#getDocumentId()}
   * @param status {@link #status}
   * @param annotation {@link #annotation}
   */
  public RagDocumentAnnotationParameters(String documentId, String status, Object annotation) {
    super(ExtractionRagAnnotationResponse.class, documentId);
    this.status = status;

    if (annotation instanceof RagAnnotation) {
      this.annotation = (RagAnnotation) annotation;
    } else if (annotation instanceof String) {
      try {
        this.annotation = mapper.readValue((String) annotation, RagAnnotation.class);
      } catch (JsonProcessingException e) {
        throw new MindeeInputException("Invalid RAG Annotation format.", e);
      }
    } else if (annotation == null) {
      this.annotation = null;
    } else {
      throw new MindeeInputException("Invalid RAG Annotation format.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getRequestParameters() {
    Map<String, Object> parameters = new HashMap<>();

    if (status != null && !status.isEmpty()) {
      parameters.put("status", status);
    }

    if (annotation != null) {
      parameters.put("annotation", annotation);
    }

    return parameters;
  }

  /**
   * Create a new builder.
   *
   * @param documentId {@link BaseAnnotationParameters#getDocumentId()}
   * @return a fresh {@link Builder}
   */
  public static Builder builder(String documentId) {
    return new Builder(documentId);
  }

  /**
   * Fluent builder for {@link RagDocumentAnnotationParameters}.
   */
  public static final class Builder extends BaseAnnotationParameters.BaseBuilder<Builder> {
    private String status;
    private Object annotation;

    Builder(String documentId) {
      super(documentId);
    }

    /** @param status {@link #status} */
    public Builder status(String status) {
      this.status = status;
      return this;
    }

    /** @param annotation {@link #annotation} */
    public Builder annotation(Object annotation) {
      this.annotation = annotation;
      return this;
    }

    /** Build an immutable {@link RagDocumentAnnotationParameters} instance. */
    public RagDocumentAnnotationParameters build() {
      return new RagDocumentAnnotationParameters(documentId, status, annotation);
    }
  }
}
