package com.mindee.v2.product.split;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
   * Returns a list of distinct page ranges. Useful for extracting pages from a document.
   */
  public List<Integer> getPageRangeDistinct() {
    return new ArrayList<>(new LinkedHashSet<>(this.pageRange));
  }
}
