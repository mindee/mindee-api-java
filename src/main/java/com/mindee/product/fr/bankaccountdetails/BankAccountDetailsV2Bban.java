package com.mindee.product.fr.bankaccountdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Full extraction of BBAN, including: branch code, bank code, account and key.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountDetailsV2Bban extends BaseField {

  /**
   * The BBAN bank code outputted as a string.
   */
  @JsonProperty("bban_bank_code")
  String bbanBankCode;
  /**
   * The BBAN branch code outputted as a string.
   */
  @JsonProperty("bban_branch_code")
  String bbanBranchCode;
  /**
   * The BBAN key outputted as a string.
   */
  @JsonProperty("bban_key")
  String bbanKey;
  /**
   * The BBAN Account number outputted as a string.
   */
  @JsonProperty("bban_number")
  String bbanNumber;

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Bank Code: %s%n", printable.get("bbanBankCode"))
        + String.format("  :Branch Code: %s%n", printable.get("bbanBranchCode"))
        + String.format("  :Key: %s%n", printable.get("bbanKey"))
        + String.format("  :Account Number: %s%n", printable.get("bbanNumber"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Bank Code: %s", printable.get("bbanBankCode"))
      + String.format(", Branch Code: %s", printable.get("bbanBranchCode"))
      + String.format(", Key: %s", printable.get("bbanKey"))
      + String.format(", Account Number: %s", printable.get("bbanNumber"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("bbanBankCode", SummaryHelper.formatForDisplay(this.bbanBankCode, null));
    printable.put("bbanBranchCode", SummaryHelper.formatForDisplay(this.bbanBranchCode, null));
    printable.put("bbanKey", SummaryHelper.formatForDisplay(this.bbanKey, null));
    printable.put("bbanNumber", SummaryHelper.formatForDisplay(this.bbanNumber, null));
    return printable;
  }
}
