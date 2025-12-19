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
 * Payslip API version 3.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipV3Document extends Prediction {

  /**
   * Information about the employee's bank account.
   */
  @JsonProperty("bank_account_details")
  protected PayslipV3BankAccountDetail bankAccountDetails;
  /**
   * Information about the employee.
   */
  @JsonProperty("employee")
  protected PayslipV3Employee employee;
  /**
   * Information about the employer.
   */
  @JsonProperty("employer")
  protected PayslipV3Employer employer;
  /**
   * Information about the employment.
   */
  @JsonProperty("employment")
  protected PayslipV3Employment employment;
  /**
   * Information about paid time off.
   */
  @JsonProperty("paid_time_off")
  protected List<PayslipV3PaidTimeOff> paidTimeOff = new ArrayList<>();
  /**
   * Detailed information about the pay.
   */
  @JsonProperty("pay_detail")
  protected PayslipV3PayDetail payDetail;
  /**
   * Information about the pay period.
   */
  @JsonProperty("pay_period")
  protected PayslipV3PayPeriod payPeriod;
  /**
   * Detailed information about the earnings.
   */
  @JsonProperty("salary_details")
  protected List<PayslipV3SalaryDetail> salaryDetails = new ArrayList<>();

  @Override
  public boolean isEmpty() {
    return (this.payPeriod == null
      && this.employee == null
      && this.employer == null
      && this.bankAccountDetails == null
      && this.employment == null
      && (this.salaryDetails == null || this.salaryDetails.isEmpty())
      && this.payDetail == null
      && (this.paidTimeOff == null || this.paidTimeOff.isEmpty()));
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(String.format(":Pay Period:%n%s", this.getPayPeriod().toFieldList()));
    outStr.append(String.format(":Employee:%n%s", this.getEmployee().toFieldList()));
    outStr.append(String.format(":Employer:%n%s", this.getEmployer().toFieldList()));
    outStr
      .append(
        String.format(":Bank Account Details:%n%s", this.getBankAccountDetails().toFieldList())
      );
    outStr.append(String.format(":Employment:%n%s", this.getEmployment().toFieldList()));
    String salaryDetailsSummary = "";
    if (!this.getSalaryDetails().isEmpty()) {
      int[] salaryDetailsColSizes = new int[] { 14, 11, 38, 8, 11 };
      salaryDetailsSummary = String
        .format("%n%s%n  ", SummaryHelper.lineSeparator(salaryDetailsColSizes, "-"))
        + "| Amount       "
        + "| Base      "
        + "| Description                          "
        + "| Number "
        + "| Rate      "
        + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(salaryDetailsColSizes, "="));
      salaryDetailsSummary += SummaryHelper
        .arrayToString(this.getSalaryDetails(), salaryDetailsColSizes);
      salaryDetailsSummary += String
        .format("%n%s", SummaryHelper.lineSeparator(salaryDetailsColSizes, "-"));
    }
    outStr.append(String.format(":Salary Details: %s%n", salaryDetailsSummary));
    outStr.append(String.format(":Pay Detail:%n%s", this.getPayDetail().toFieldList()));
    String paidTimeOffSummary = "";
    if (!this.getPaidTimeOff().isEmpty()) {
      int[] paidTimeOffColSizes = new int[] { 11, 8, 13, 11, 11 };
      paidTimeOffSummary = String
        .format("%n%s%n  ", SummaryHelper.lineSeparator(paidTimeOffColSizes, "-"))
        + "| Accrued   "
        + "| Period "
        + "| Type        "
        + "| Remaining "
        + "| Used      "
        + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(paidTimeOffColSizes, "="));
      paidTimeOffSummary += SummaryHelper.arrayToString(this.getPaidTimeOff(), paidTimeOffColSizes);
      paidTimeOffSummary += String
        .format("%n%s", SummaryHelper.lineSeparator(paidTimeOffColSizes, "-"));
    }
    outStr.append(String.format(":Paid Time Off: %s%n", paidTimeOffSummary));
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
