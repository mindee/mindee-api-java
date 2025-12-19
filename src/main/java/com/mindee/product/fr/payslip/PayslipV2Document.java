package com.mindee.product.fr.payslip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Payslip API version 2.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipV2Document extends Prediction {

  /**
   * Information about the employee's bank account.
   */
  @JsonProperty("bank_account_details")
  protected PayslipV2BankAccountDetail bankAccountDetails;
  /**
   * Information about the employee.
   */
  @JsonProperty("employee")
  protected PayslipV2Employee employee;
  /**
   * Information about the employer.
   */
  @JsonProperty("employer")
  protected PayslipV2Employer employer;
  /**
   * Information about the employment.
   */
  @JsonProperty("employment")
  protected PayslipV2Employment employment;
  /**
   * Detailed information about the pay.
   */
  @JsonProperty("pay_detail")
  protected PayslipV2PayDetail payDetail;
  /**
   * Information about the pay period.
   */
  @JsonProperty("pay_period")
  protected PayslipV2PayPeriod payPeriod;
  /**
   * Information about paid time off.
   */
  @JsonProperty("pto")
  protected PayslipV2Pto pto;
  /**
   * Detailed information about the earnings.
   */
  @JsonProperty("salary_details")
  protected List<PayslipV2SalaryDetail> salaryDetails = new ArrayList<>();

  @Override
  public boolean isEmpty() {
    return (this.employee == null
      && this.employer == null
      && this.bankAccountDetails == null
      && this.employment == null
      && (this.salaryDetails == null || this.salaryDetails.isEmpty())
      && this.payDetail == null
      && this.pto == null
      && this.payPeriod == null);
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(String.format(":Employee:%n%s", this.getEmployee().toFieldList()));
    outStr.append(String.format(":Employer:%n%s", this.getEmployer().toFieldList()));
    outStr
      .append(
        String.format(":Bank Account Details:%n%s", this.getBankAccountDetails().toFieldList())
      );
    outStr.append(String.format(":Employment:%n%s", this.getEmployment().toFieldList()));
    String salaryDetailsSummary = "";
    if (!this.getSalaryDetails().isEmpty()) {
      int[] salaryDetailsColSizes = new int[] { 14, 11, 38, 11 };
      salaryDetailsSummary = String
        .format("%n%s%n  ", SummaryHelper.lineSeparator(salaryDetailsColSizes, "-"))
        + "| Amount       "
        + "| Base      "
        + "| Description                          "
        + "| Rate      "
        + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(salaryDetailsColSizes, "="));
      salaryDetailsSummary += SummaryHelper
        .arrayToString(this.getSalaryDetails(), salaryDetailsColSizes);
      salaryDetailsSummary += String
        .format("%n%s", SummaryHelper.lineSeparator(salaryDetailsColSizes, "-"));
    }
    outStr.append(String.format(":Salary Details: %s%n", salaryDetailsSummary));
    outStr.append(String.format(":Pay Detail:%n%s", this.getPayDetail().toFieldList()));
    outStr.append(String.format(":PTO:%n%s", this.getPto().toFieldList()));
    outStr.append(String.format(":Pay Period:%n%s", this.getPayPeriod().toFieldList()));
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
