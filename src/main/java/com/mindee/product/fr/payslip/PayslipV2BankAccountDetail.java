package com.mindee.product.fr.payslip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Information about the employee's bank account.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipV2BankAccountDetail extends BaseField {

  /**
   * The name of the bank.
   */
  @JsonProperty("bank_name")
  String bankName;
  /**
   * The IBAN of the bank account.
   */
  @JsonProperty("iban")
  String iban;
  /**
   * The SWIFT code of the bank.
   */
  @JsonProperty("swift")
  String swift;

  public boolean isEmpty() {
    return ((bankName == null || bankName.isEmpty())
      && (iban == null || iban.isEmpty())
      && (swift == null || swift.isEmpty()));
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Bank Name: %s%n", printable.get("bankName"))
      + String.format("  :IBAN: %s%n", printable.get("iban"))
      + String.format("  :SWIFT: %s%n", printable.get("swift"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Bank Name: %s", printable.get("bankName"))
      + String.format(", IBAN: %s", printable.get("iban"))
      + String.format(", SWIFT: %s", printable.get("swift"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("bankName", SummaryHelper.formatForDisplay(this.bankName, null));
    printable.put("iban", SummaryHelper.formatForDisplay(this.iban, null));
    printable.put("swift", SummaryHelper.formatForDisplay(this.swift, null));
    return printable;
  }
}
