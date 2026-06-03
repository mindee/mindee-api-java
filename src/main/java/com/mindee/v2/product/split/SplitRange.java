package com.mindee.v2.product.split;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.image.ExtractedImages;
import com.mindee.input.LocalInputSource;
import com.mindee.pdf.ExtractedPDF;
import com.mindee.v2.fileoperations.Split;
import com.mindee.v2.product.extraction.ExtractionResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A single document as identified when splitting a multi-document source file.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class SplitRange {
  /**
   * 0-based page indexes, where the first integer indicates the start page and the second integer
   * indicates the end page.
   */
  @JsonProperty("page_range")
  public List<Integer> pageRange;

  /**
   * The document type, as identified on given classification values.
   */
  @JsonProperty("document_type")
  public String documentType;

  /**
   * The extraction response associated with the split.
   */
  @JsonProperty("extraction_response")
  private ExtractionResponse extractionResponse;

  /**
   * Returns a list of distinct page ranges. Useful for extracting pages from a document.
   */
  public List<Integer> getPageRangeDistinct() {
    return new ArrayList<>(new LinkedHashSet<>(this.pageRange));
  }

  /**
   * Based on the crop results, extract the documents into individual files as an
   * {@link ExtractedImages} instance.
   */
  public ExtractedPDF extractFromInputSource(LocalInputSource inputSource) throws IOException {
    var splitter = new Split(inputSource);
    return splitter.extractSingleSplit(this);
  }
}
