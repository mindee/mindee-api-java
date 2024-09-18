package com.mindee.product.fr.payslip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Information about the employer.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipV2Employer extends BaseField {

  /**
   * The address of the employer.
   */
  @JsonProperty("address")
  String address;
  /**
   * The company ID of the employer.
   */
  @JsonProperty("company_id")
  String companyId;
  /**
   * The site of the company.
   */
  @JsonProperty("company_site")
  String companySite;
  /**
   * The NAF code of the employer.
   */
  @JsonProperty("naf_code")
  String nafCode;
  /**
   * The name of the employer.
   */
  @JsonProperty("name")
  String name;
  /**
   * The phone number of the employer.
   */
  @JsonProperty("phone_number")
  String phoneNumber;
  /**
   * The URSSAF number of the employer.
   */
  @JsonProperty("urssaf_number")
  String urssafNumber;

  public boolean isEmpty() {
    return (
        (address == null || address.isEmpty())
        && (companyId == null || companyId.isEmpty())
        && (companySite == null || companySite.isEmpty())
        && (nafCode == null || nafCode.isEmpty())
        && (name == null || name.isEmpty())
        && (phoneNumber == null || phoneNumber.isEmpty())
        && (urssafNumber == null || urssafNumber.isEmpty())
      );
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Address: %s%n", printable.get("address"))
        + String.format("  :Company ID: %s%n", printable.get("companyId"))
        + String.format("  :Company Site: %s%n", printable.get("companySite"))
        + String.format("  :NAF Code: %s%n", printable.get("nafCode"))
        + String.format("  :Name: %s%n", printable.get("name"))
        + String.format("  :Phone Number: %s%n", printable.get("phoneNumber"))
        + String.format("  :URSSAF Number: %s%n", printable.get("urssafNumber"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Address: %s", printable.get("address"))
      + String.format(", Company ID: %s", printable.get("companyId"))
      + String.format(", Company Site: %s", printable.get("companySite"))
      + String.format(", NAF Code: %s", printable.get("nafCode"))
      + String.format(", Name: %s", printable.get("name"))
      + String.format(", Phone Number: %s", printable.get("phoneNumber"))
      + String.format(", URSSAF Number: %s", printable.get("urssafNumber"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("address", SummaryHelper.formatForDisplay(this.address, null));
    printable.put("companyId", SummaryHelper.formatForDisplay(this.companyId, null));
    printable.put("companySite", SummaryHelper.formatForDisplay(this.companySite, null));
    printable.put("nafCode", SummaryHelper.formatForDisplay(this.nafCode, null));
    printable.put("name", SummaryHelper.formatForDisplay(this.name, null));
    printable.put("phoneNumber", SummaryHelper.formatForDisplay(this.phoneNumber, null));
    printable.put("urssafNumber", SummaryHelper.formatForDisplay(this.urssafNumber, null));
    return printable;
  }
}
