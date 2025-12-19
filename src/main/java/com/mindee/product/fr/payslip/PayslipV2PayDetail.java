package com.mindee.product.fr.payslip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Detailed information about the pay.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipV2PayDetail extends BaseField {

  /**
   * The gross salary of the employee.
   */
  @JsonProperty("gross_salary")
  Double grossSalary;
  /**
   * The year-to-date gross salary of the employee.
   */
  @JsonProperty("gross_salary_ytd")
  Double grossSalaryYtd;
  /**
   * The income tax rate of the employee.
   */
  @JsonProperty("income_tax_rate")
  Double incomeTaxRate;
  /**
   * The income tax withheld from the employee's pay.
   */
  @JsonProperty("income_tax_withheld")
  Double incomeTaxWithheld;
  /**
   * The net paid amount of the employee.
   */
  @JsonProperty("net_paid")
  Double netPaid;
  /**
   * The net paid amount before tax of the employee.
   */
  @JsonProperty("net_paid_before_tax")
  Double netPaidBeforeTax;
  /**
   * The net taxable amount of the employee.
   */
  @JsonProperty("net_taxable")
  Double netTaxable;
  /**
   * The year-to-date net taxable amount of the employee.
   */
  @JsonProperty("net_taxable_ytd")
  Double netTaxableYtd;
  /**
   * The total cost to the employer.
   */
  @JsonProperty("total_cost_employer")
  Double totalCostEmployer;
  /**
   * The total taxes and deductions of the employee.
   */
  @JsonProperty("total_taxes_and_deductions")
  Double totalTaxesAndDeductions;

  public boolean isEmpty() {
    return (grossSalary == null
      && grossSalaryYtd == null
      && incomeTaxRate == null
      && incomeTaxWithheld == null
      && netPaid == null
      && netPaidBeforeTax == null
      && netTaxable == null
      && netTaxableYtd == null
      && totalCostEmployer == null
      && totalTaxesAndDeductions == null);
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Gross Salary: %s%n", printable.get("grossSalary"))
      + String.format("  :Gross Salary YTD: %s%n", printable.get("grossSalaryYtd"))
      + String.format("  :Income Tax Rate: %s%n", printable.get("incomeTaxRate"))
      + String.format("  :Income Tax Withheld: %s%n", printable.get("incomeTaxWithheld"))
      + String.format("  :Net Paid: %s%n", printable.get("netPaid"))
      + String.format("  :Net Paid Before Tax: %s%n", printable.get("netPaidBeforeTax"))
      + String.format("  :Net Taxable: %s%n", printable.get("netTaxable"))
      + String.format("  :Net Taxable YTD: %s%n", printable.get("netTaxableYtd"))
      + String.format("  :Total Cost Employer: %s%n", printable.get("totalCostEmployer"))
      + String
        .format("  :Total Taxes and Deductions: %s%n", printable.get("totalTaxesAndDeductions"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Gross Salary: %s", printable.get("grossSalary"))
      + String.format(", Gross Salary YTD: %s", printable.get("grossSalaryYtd"))
      + String.format(", Income Tax Rate: %s", printable.get("incomeTaxRate"))
      + String.format(", Income Tax Withheld: %s", printable.get("incomeTaxWithheld"))
      + String.format(", Net Paid: %s", printable.get("netPaid"))
      + String.format(", Net Paid Before Tax: %s", printable.get("netPaidBeforeTax"))
      + String.format(", Net Taxable: %s", printable.get("netTaxable"))
      + String.format(", Net Taxable YTD: %s", printable.get("netTaxableYtd"))
      + String.format(", Total Cost Employer: %s", printable.get("totalCostEmployer"))
      + String.format(", Total Taxes and Deductions: %s", printable.get("totalTaxesAndDeductions"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("grossSalary", SummaryHelper.formatAmount(this.grossSalary));
    printable.put("grossSalaryYtd", SummaryHelper.formatAmount(this.grossSalaryYtd));
    printable.put("incomeTaxRate", SummaryHelper.formatAmount(this.incomeTaxRate));
    printable.put("incomeTaxWithheld", SummaryHelper.formatAmount(this.incomeTaxWithheld));
    printable.put("netPaid", SummaryHelper.formatAmount(this.netPaid));
    printable.put("netPaidBeforeTax", SummaryHelper.formatAmount(this.netPaidBeforeTax));
    printable.put("netTaxable", SummaryHelper.formatAmount(this.netTaxable));
    printable.put("netTaxableYtd", SummaryHelper.formatAmount(this.netTaxableYtd));
    printable.put("totalCostEmployer", SummaryHelper.formatAmount(this.totalCostEmployer));
    printable
      .put("totalTaxesAndDeductions", SummaryHelper.formatAmount(this.totalTaxesAndDeductions));
    return printable;
  }
}
