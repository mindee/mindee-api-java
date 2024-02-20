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
 * The list of certificates obtained by the candidate.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeV1Certificate extends BaseField implements LineItemField {

  /**
   * The grade obtained for the certificate.
   */
  @JsonProperty("grade")
  String grade;
  /**
   * The name of certifications obtained by the individual.
   */
  @JsonProperty("name")
  String name;
  /**
   * The organization or institution that issued the certificates listed in the document.
   */
  @JsonProperty("provider")
  String provider;
  /**
   * The year when a certificate was issued or received.
   */
  @JsonProperty("year")
  String year;

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.printableValues();
    return String.format("| %-10s ", printable.get("grade"))
      + String.format("| %-30s ", printable.get("name"))
      + String.format("| %-25s ", printable.get("provider"))
      + String.format("| %-4s |", printable.get("year"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Grade: %s", printable.get("grade"))
      + String.format(", Name: %s", printable.get("name"))
      + String.format(", Provider: %s", printable.get("provider"))
      + String.format(", Year: %s", printable.get("year"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("grade", SummaryHelper.formatForDisplay(this.grade, 10));
    printable.put("name", SummaryHelper.formatForDisplay(this.name, 30));
    printable.put("provider", SummaryHelper.formatForDisplay(this.provider, 25));
    printable.put("year", SummaryHelper.formatForDisplay(this.year, null));
    return printable;
  }
}
