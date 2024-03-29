package com.mindee.parsing.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represent a `predict` response from Mindee API.
 *
 * @param <T> Set the prediction model used to parse the document. The response object will be
 *            instantiated based on this parameter.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsyncPredictResponse<T extends Inference> extends ApiResponse {
  /**
   * Set the prediction model used to parse the document.
   * The response object will be instantiated based on this parameter.
   */
  @JsonProperty("document")
  Document<T> document;
  /**
   * Details for the submitted job.
   */
  @JsonProperty("job")
  private Job job;

  /**
   * Get the document optional.
   */
  public Optional<Document<T>> getDocument() {
    return Optional.ofNullable(this.document);
  }

  /**
   * Get the document object if it is present.
   * Shortcut for when the document optional is known to be present.
   */
  public Document<T> getDocumentObj() {
    if (getDocument().isPresent()) {
      return this.document;
    }
    return null;
  }

  public String toString() {
    String document;
    if (this.getDocument().isPresent()) {
      document = String.format("%n%n%s", this.getDocument().get());
    } else {
      document = "";
    }
    return String.format("%s%n%n%s%s", this.apiRequest, this.job, document);
  }
}
