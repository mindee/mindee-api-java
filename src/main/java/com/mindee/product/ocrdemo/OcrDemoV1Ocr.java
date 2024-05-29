package com.mindee.product.ocrdemo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Full text separated by ' ' for words and '\n' for lines
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OcrDemoV1Ocr extends BaseField {

  /**
   * The ocr full text.
   */
  @JsonProperty("ocr_full_text")
  String ocrFullText;

  public boolean isEmpty() {
    return (
      (ocrFullText == null || ocrFullText.isEmpty())
    );
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :OCR Full Text: %s%n", printable.get("ocrFullText"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("OCR Full Text: %s", printable.get("ocrFullText"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("ocrFullText", SummaryHelper.formatString(this.ocrFullText));
    return printable;
  }
}
