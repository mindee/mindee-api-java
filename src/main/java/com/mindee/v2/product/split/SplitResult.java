package com.mindee.v2.product.split;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.input.LocalInputSource;
import com.mindee.pdf.ExtractedPDFs;
import com.mindee.v2.fileoperations.Split;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Result of the document splitter inference.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class SplitResult {
  /**
   * List of documents identified within a multi-document source file.
   */
  @JsonProperty("splits")
  private ArrayList<SplitRange> splits;

  /**
   * Based on the split results, extract the documents into individual files as an
   * {@link ExtractedPDFs} instance.
   */
  public ExtractedPDFs extractFromInputSource(LocalInputSource inputSource) throws IOException {
    var splitter = new Split(inputSource);
    return splitter.extractMultipleSplits(this.splits);
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner("\n");
    joiner.add("Splits\n======");
    for (SplitRange item : splits) {
      joiner.add(item.toString());
    }
    return joiner.toString();
  }
}
