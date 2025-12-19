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
 * Generic result for any off-the-shelf Mindee V2 model.
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
   * Raw text extracted from all pages in the document.
   */
  @JsonProperty("raw_text")
  private RawText rawText;

  /**
   * RAG metadata.
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
