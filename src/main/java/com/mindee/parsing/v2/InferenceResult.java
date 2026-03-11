package com.mindee.parsing.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.v2.field.InferenceFields;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The inference result for an extraction request.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class InferenceResult {

  /**
   * Extracted fields, the key corresponds to the field's name in the data schema.
   */
  @JsonProperty("fields")
  private InferenceFields fields;

  /**
   * If 'rawText' was activated, contains the extracted text of the document.
   */
  @JsonProperty("raw_text")
  private RawText rawText;

  /**
   * If 'rag' was activated, contains metadata about the RAG operation.
   */
  @JsonProperty("rag")
  private RagMetadata rag;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    joiner.add("Fields").add("======");
    joiner.add(fields.toString());

    return joiner.toString();
  }
}
