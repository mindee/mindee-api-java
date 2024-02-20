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
 * The list of values that represent the professional experiences of an individual in their global resume.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeV1ProfessionalExperience extends BaseField implements LineItemField {

  /**
   * The type of contract for a professional experience. Possible values: 'Full-Time', 'Part-Time', 'Internship' and 'Freelance'.
   */
  @JsonProperty("contract_type")
  String contractType;
  /**
   * The specific department or division within a company where the professional experience was gained.
   */
  @JsonProperty("department")
  String department;
  /**
   * The name of the company or organization where the candidate has worked.
   */
  @JsonProperty("employer")
  String employer;
  /**
   * The month when a professional experience ended.
   */
  @JsonProperty("end_month")
  String endMonth;
  /**
   * The year when a professional experience ended.
   */
  @JsonProperty("end_year")
  String endYear;
  /**
   * The position or job title held by the individual in their previous work experience.
   */
  @JsonProperty("role")
  String role;
  /**
   * The month when a professional experience began.
   */
  @JsonProperty("start_month")
  String startMonth;
  /**
   * The year when a professional experience began.
   */
  @JsonProperty("start_year")
  String startYear;

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    Map<String, String> printable = this.printableValues();
    return String.format("| %-15s ", printable.get("contractType"))
      + String.format("| %-10s ", printable.get("department"))
      + String.format("| %-25s ", printable.get("employer"))
      + String.format("| %-9s ", printable.get("endMonth"))
      + String.format("| %-8s ", printable.get("endYear"))
      + String.format("| %-20s ", printable.get("role"))
      + String.format("| %-11s ", printable.get("startMonth"))
      + String.format("| %-10s |", printable.get("startYear"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Contract Type: %s", printable.get("contractType"))
      + String.format(", Department: %s", printable.get("department"))
      + String.format(", Employer: %s", printable.get("employer"))
      + String.format(", End Month: %s", printable.get("endMonth"))
      + String.format(", End Year: %s", printable.get("endYear"))
      + String.format(", Role: %s", printable.get("role"))
      + String.format(", Start Month: %s", printable.get("startMonth"))
      + String.format(", Start Year: %s", printable.get("startYear"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("contractType", SummaryHelper.formatForDisplay(this.contractType, 15));
    printable.put("department", SummaryHelper.formatForDisplay(this.department, 10));
    printable.put("employer", SummaryHelper.formatForDisplay(this.employer, 25));
    printable.put("endMonth", SummaryHelper.formatForDisplay(this.endMonth, null));
    printable.put("endYear", SummaryHelper.formatForDisplay(this.endYear, null));
    printable.put("role", SummaryHelper.formatForDisplay(this.role, 20));
    printable.put("startMonth", SummaryHelper.formatForDisplay(this.startMonth, null));
    printable.put("startYear", SummaryHelper.formatForDisplay(this.startYear, null));
    return printable;
  }
}
