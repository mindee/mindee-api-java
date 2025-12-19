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
 * The list of the candidate's educational background.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeV1Education extends BaseField implements LineItemField {

  /**
   * The area of study or specialization.
   */
  @JsonProperty("degree_domain")
  String degreeDomain;
  /**
   * The type of degree obtained, such as Bachelor's, Master's, or Doctorate.
   */
  @JsonProperty("degree_type")
  String degreeType;
  /**
   * The month when the education program or course was completed.
   */
  @JsonProperty("end_month")
  String endMonth;
  /**
   * The year when the education program or course was completed.
   */
  @JsonProperty("end_year")
  String endYear;
  /**
   * The name of the school.
   */
  @JsonProperty("school")
  String school;
  /**
   * The month when the education program or course began.
   */
  @JsonProperty("start_month")
  String startMonth;
  /**
   * The year when the education program or course began.
   */
  @JsonProperty("start_year")
  String startYear;

  public boolean isEmpty() {
    return ((degreeDomain == null || degreeDomain.isEmpty())
      && (degreeType == null || degreeType.isEmpty())
      && (endMonth == null || endMonth.isEmpty())
      && (endYear == null || endYear.isEmpty())
      && (school == null || school.isEmpty())
      && (startMonth == null || startMonth.isEmpty())
      && (startYear == null || startYear.isEmpty()));
  }

  private Map<String, String> tablePrintableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("degreeDomain", SummaryHelper.formatForDisplay(this.degreeDomain, 15));
    printable.put("degreeType", SummaryHelper.formatForDisplay(this.degreeType, 25));
    printable.put("endMonth", SummaryHelper.formatForDisplay(this.endMonth, null));
    printable.put("endYear", SummaryHelper.formatForDisplay(this.endYear, null));
    printable.put("school", SummaryHelper.formatForDisplay(this.school, 25));
    printable.put("startMonth", SummaryHelper.formatForDisplay(this.startMonth, null));
    printable.put("startYear", SummaryHelper.formatForDisplay(this.startYear, null));
    return printable;
  }

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.tablePrintableValues();
    return String.format("| %-15s ", printable.get("degreeDomain"))
      + String.format("| %-25s ", printable.get("degreeType"))
      + String.format("| %-9s ", printable.get("endMonth"))
      + String.format("| %-8s ", printable.get("endYear"))
      + String.format("| %-25s ", printable.get("school"))
      + String.format("| %-11s ", printable.get("startMonth"))
      + String.format("| %-10s |", printable.get("startYear"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Domain: %s", printable.get("degreeDomain"))
      + String.format(", Degree: %s", printable.get("degreeType"))
      + String.format(", End Month: %s", printable.get("endMonth"))
      + String.format(", End Year: %s", printable.get("endYear"))
      + String.format(", School: %s", printable.get("school"))
      + String.format(", Start Month: %s", printable.get("startMonth"))
      + String.format(", Start Year: %s", printable.get("startYear"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("degreeDomain", SummaryHelper.formatForDisplay(this.degreeDomain, null));
    printable.put("degreeType", SummaryHelper.formatForDisplay(this.degreeType, null));
    printable.put("endMonth", SummaryHelper.formatForDisplay(this.endMonth, null));
    printable.put("endYear", SummaryHelper.formatForDisplay(this.endYear, null));
    printable.put("school", SummaryHelper.formatForDisplay(this.school, null));
    printable.put("startMonth", SummaryHelper.formatForDisplay(this.startMonth, null));
    printable.put("startYear", SummaryHelper.formatForDisplay(this.startYear, null));
    return printable;
  }
}
