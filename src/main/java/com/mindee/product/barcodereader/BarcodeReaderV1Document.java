package com.mindee.product.barcodereader;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Barcode Reader, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class BarcodeReaderV1Document {


  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
