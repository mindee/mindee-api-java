package com.mindee.product.resume;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import com.mindee.parsing.standard.LineItemField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The list of languages that the candidate is proficient in.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeV1Language extends BaseField implements LineItemField {

  /**
   * The language's ISO 639 code.
   */
  @JsonProperty("language")
  String language;
  /**
   * The candidate's level for the language.
   */
  @JsonProperty("level")
  String level;

  public boolean isEmpty() {
    return (
        (language == null || language.isEmpty())
        && (level == null || level.isEmpty())
      );
  }

  private Map<String, String> tablePrintableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("language", SummaryHelper.formatForDisplay(this.language, null));
    printable.put("level", SummaryHelper.formatForDisplay(this.level, 20));
    return printable;
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.tablePrintableValues();
    return String.format("| %-8s ", printable.get("language"))
      + String.format("| %-20s |", printable.get("level"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Language: %s", printable.get("language"))
      + String.format(", Level: %s", printable.get("level"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("language", SummaryHelper.formatForDisplay(this.language, null));
    printable.put("level", SummaryHelper.formatForDisplay(this.level, null));
    return printable;
  }
}
