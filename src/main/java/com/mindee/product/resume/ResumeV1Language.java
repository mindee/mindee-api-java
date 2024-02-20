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
 * The list of languages that a person is proficient in, as stated in their resume.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeV1Language extends BaseField implements LineItemField {

  /**
   * The language ISO 639 code.
   */
  @JsonProperty("language")
  String language;
  /**
   * The level for the language. Possible values: 'Fluent', 'Proficient', 'Intermediate' and 'Beginner'.
   */
  @JsonProperty("level")
  String level;

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.printableValues();
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
    printable.put("level", SummaryHelper.formatForDisplay(this.level, 20));
    return printable;
  }
}
