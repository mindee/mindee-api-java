package com.mindee.v2.product.classification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Result of the document classifier inference.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class ClassificationResult {
  /**
   * Classification of the document type from the source file.
   */
  @JsonProperty("classification")
  private ClassificationClassifier classification;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    joiner.add("Classification\n==============");
    joiner.add(classification.toString());

    return joiner.toString();
  }
}
