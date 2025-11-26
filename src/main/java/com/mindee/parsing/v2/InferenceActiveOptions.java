package com.mindee.parsing.v2;

import static com.mindee.parsing.SummaryHelper.formatForDisplay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.StringJoiner;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Option response for V2 API inference.
 */
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InferenceActiveOptions {

  @JsonProperty("rag")
  private boolean rag;

  @JsonProperty("raw_text")
  private boolean rawText;

  @JsonProperty("polygon")
  private boolean polygon;

  @JsonProperty("confidence")
  private boolean confidence;

  /**
   *  Data schema options provided for the inference.
   */
  @Getter
  @JsonProperty("data_schema")
  private DataSchemaActiveOptions dataSchema;

  /**
   * Whether the RAG feature was activated.
   */
  public boolean getRag() {
    return rag;
  }

  /**
   * Whether the Raw Text feature was activated.
   */
  public boolean getRawText() {
    return rawText;
  }

  /**
   * Whether the polygon feature was activated.
   */
  public boolean getPolygon() {
    return polygon;
  }

  /**
   * Whether the confidence feature was activated.
   */
  public boolean getConfidence() {
    return confidence;
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    return joiner
        .add("Active Options")
        .add("==============")
        .add(":Raw Text: " + formatForDisplay(rawText, 5))
        .add(":Polygon: " + formatForDisplay(polygon, 5))
        .add(":Confidence: " + formatForDisplay(confidence, 5))
        .add(":RAG: " + formatForDisplay(rag, 5))
        .toString();
  }
}
