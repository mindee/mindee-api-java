package com.mindee.product.ocrdemo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Page data for OCR Demo, API version 1.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrDemoV1Page extends OcrDemoV1Document {

  /**
   * Full text separated by ' ' for words and '\n' for lines
   */
  @JsonProperty("ocr")
  private OcrDemoV1Ocr ocr;

  @Override
  public boolean isEmpty() {
    return (
      this.ocr == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":OCR:%n%s", this.getOcr().toFieldList())
    );
    outStr.append(super.toString());
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
