package com.mindee.v2.product.classification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Classification of the document type from the source file.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassificationClassifier {
  /**
   * The document type, as identified on given classification values.
   */
  @JsonProperty("document_type")
  private String documentType;

  @Override
  public String toString() {
    return "Document Type: " + documentType;
  }
}
