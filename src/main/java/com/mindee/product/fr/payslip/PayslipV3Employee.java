package com.mindee.product.fr.payslip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.BaseField;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Information about the employee.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayslipV3Employee extends BaseField {

  /**
   * The address of the employee.
   */
  @JsonProperty("address")
  String address;
  /**
   * The date of birth of the employee.
   */
  @JsonProperty("date_of_birth")
  String dateOfBirth;
  /**
   * The first name of the employee.
   */
  @JsonProperty("first_name")
  String firstName;
  /**
   * The last name of the employee.
   */
  @JsonProperty("last_name")
  String lastName;
  /**
   * The phone number of the employee.
   */
  @JsonProperty("phone_number")
  String phoneNumber;
  /**
   * The registration number of the employee.
   */
  @JsonProperty("registration_number")
  String registrationNumber;
  /**
   * The social security number of the employee.
   */
  @JsonProperty("social_security_number")
  String socialSecurityNumber;

  public boolean isEmpty() {
    return (
        (address == null || address.isEmpty())
        && (dateOfBirth == null || dateOfBirth.isEmpty())
        && (firstName == null || firstName.isEmpty())
        && (lastName == null || lastName.isEmpty())
        && (phoneNumber == null || phoneNumber.isEmpty())
        && (registrationNumber == null || registrationNumber.isEmpty())
        && (socialSecurityNumber == null || socialSecurityNumber.isEmpty())
      );
  }

  /**
   * Output the object in a format suitable for inclusion in an rST field list.
   */
  public String toFieldList() {
    Map<String, String> printable = this.printableValues();
    return String.format("  :Address: %s%n", printable.get("address"))
        + String.format("  :Date of Birth: %s%n", printable.get("dateOfBirth"))
        + String.format("  :First Name: %s%n", printable.get("firstName"))
        + String.format("  :Last Name: %s%n", printable.get("lastName"))
        + String.format("  :Phone Number: %s%n", printable.get("phoneNumber"))
        + String.format("  :Registration Number: %s%n", printable.get("registrationNumber"))
        + String.format("  :Social Security Number: %s%n", printable.get("socialSecurityNumber"));
  }

  @Override
  public String toString() {
    Map<String, String> printable = this.printableValues();
    return String.format("Address: %s", printable.get("address"))
      + String.format(", Date of Birth: %s", printable.get("dateOfBirth"))
      + String.format(", First Name: %s", printable.get("firstName"))
      + String.format(", Last Name: %s", printable.get("lastName"))
      + String.format(", Phone Number: %s", printable.get("phoneNumber"))
      + String.format(", Registration Number: %s", printable.get("registrationNumber"))
      + String.format(", Social Security Number: %s", printable.get("socialSecurityNumber"));
  }

  private Map<String, String> printableValues() {
    Map<String, String> printable = new HashMap<>();

    printable.put("address", SummaryHelper.formatForDisplay(this.address, null));
    printable.put("dateOfBirth", SummaryHelper.formatForDisplay(this.dateOfBirth, null));
    printable.put("firstName", SummaryHelper.formatForDisplay(this.firstName, null));
    printable.put("lastName", SummaryHelper.formatForDisplay(this.lastName, null));
    printable.put("phoneNumber", SummaryHelper.formatForDisplay(this.phoneNumber, null));
    printable.put("registrationNumber", SummaryHelper.formatForDisplay(this.registrationNumber, null));
    printable.put("socialSecurityNumber", SummaryHelper.formatForDisplay(this.socialSecurityNumber, null));
    return printable;
  }
}
