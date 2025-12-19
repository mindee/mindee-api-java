package com.mindee.product.fr.payslip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Information about the employment.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipV3Employment extends BaseField {

  /**
   * The category of the employment.
   */
  @JsonProperty("category")
  String category;
  /**
   * The coefficient of the employment.
   */
  @JsonProperty("coefficient")
  String coefficient;
  /**
   * The collective agreement of the employment.
   */
  @JsonProperty("collective_agreement")
  String collectiveAgreement;
  /**
   * The job title of the employee.
   */
  @JsonProperty("job_title")
  String jobTitle;
  /**
   * The position level of the employment.
   */
  @JsonProperty("position_level")
  String positionLevel;
  /**
   * The seniority date of the employment.
   */
  @JsonProperty("seniority_date")
  String seniorityDate;
  /**
   * The start date of the employment.
   */
  @JsonProperty("start_date")
  String startDate;

  public boolean isEmpty() {
    return ((category == null || category.isEmpty())
      && (coefficient == null || coefficient.isEmpty())
      && (collectiveAgreement == null || collectiveAgreement.isEmpty())
      && (jobTitle == null || jobTitle.isEmpty())
      && (positionLevel == null || positionLevel.isEmpty())
      && (seniorityDate == null || seniorityDate.isEmpty())
      && (startDate == null || startDate.isEmpty()));
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Category: %s%n", printable.get("category"))
      + String.format("  :Coefficient: %s%n", printable.get("coefficient"))
      + String.format("  :Collective Agreement: %s%n", printable.get("collectiveAgreement"))
      + String.format("  :Job Title: %s%n", printable.get("jobTitle"))
      + String.format("  :Position Level: %s%n", printable.get("positionLevel"))
      + String.format("  :Seniority Date: %s%n", printable.get("seniorityDate"))
      + String.format("  :Start Date: %s%n", printable.get("startDate"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Category: %s", printable.get("category"))
      + String.format(", Coefficient: %s", printable.get("coefficient"))
      + String.format(", Collective Agreement: %s", printable.get("collectiveAgreement"))
      + String.format(", Job Title: %s", printable.get("jobTitle"))
      + String.format(", Position Level: %s", printable.get("positionLevel"))
      + String.format(", Seniority Date: %s", printable.get("seniorityDate"))
      + String.format(", Start Date: %s", printable.get("startDate"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("category", SummaryHelper.formatForDisplay(this.category, null));
    printable.put("coefficient", SummaryHelper.formatForDisplay(this.coefficient, null));
    printable
      .put("collectiveAgreement", SummaryHelper.formatForDisplay(this.collectiveAgreement, null));
    printable.put("jobTitle", SummaryHelper.formatForDisplay(this.jobTitle, null));
    printable.put("positionLevel", SummaryHelper.formatForDisplay(this.positionLevel, null));
    printable.put("seniorityDate", SummaryHelper.formatForDisplay(this.seniorityDate, null));
    printable.put("startDate", SummaryHelper.formatForDisplay(this.startDate, null));
    return printable;
  }
}
