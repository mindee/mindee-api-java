package com.mindee.product.eu.driverlicense;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Driver License API version 1.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverLicenseV1Document extends Prediction {

  /**
   * EU driver license holders address
   */
  @JsonProperty("address")
  protected StringField address;
  /**
   * EU driver license holders categories
   */
  @JsonProperty("category")
  protected StringField category;
  /**
   * Country code extracted as a string.
   */
  @JsonProperty("country_code")
  protected StringField countryCode;
  /**
   * The date of birth of the document holder
   */
  @JsonProperty("date_of_birth")
  protected DateField dateOfBirth;
  /**
   * ID number of the Document.
   */
  @JsonProperty("document_id")
  protected StringField documentId;
  /**
   * Date the document expires
   */
  @JsonProperty("expiry_date")
  protected DateField expiryDate;
  /**
   * First name(s) of the driver license holder
   */
  @JsonProperty("first_name")
  protected StringField firstName;
  /**
   * Authority that issued the document
   */
  @JsonProperty("issue_authority")
  protected StringField issueAuthority;
  /**
   * Date the document was issued
   */
  @JsonProperty("issue_date")
  protected DateField issueDate;
  /**
   * Last name of the driver license holder.
   */
  @JsonProperty("last_name")
  protected StringField lastName;
  /**
   * Machine-readable license number
   */
  @JsonProperty("mrz")
  protected StringField mrz;
  /**
   * Place where the driver license holder was born
   */
  @JsonProperty("place_of_birth")
  protected StringField placeOfBirth;

  @Override
  public boolean isEmpty() {
    return (
      this.countryCode == null
      && this.documentId == null
      && this.category == null
      && this.lastName == null
      && this.firstName == null
      && this.dateOfBirth == null
      && this.placeOfBirth == null
      && this.expiryDate == null
      && this.issueDate == null
      && this.issueAuthority == null
      && this.mrz == null
      && this.address == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Country Code: %s%n", this.getCountryCode())
    );
    outStr.append(
        String.format(":Document ID: %s%n", this.getDocumentId())
    );
    outStr.append(
        String.format(":Driver License Category: %s%n", this.getCategory())
    );
    outStr.append(
        String.format(":Last Name: %s%n", this.getLastName())
    );
    outStr.append(
        String.format(":First Name: %s%n", this.getFirstName())
    );
    outStr.append(
        String.format(":Date Of Birth: %s%n", this.getDateOfBirth())
    );
    outStr.append(
        String.format(":Place Of Birth: %s%n", this.getPlaceOfBirth())
    );
    outStr.append(
        String.format(":Expiry Date: %s%n", this.getExpiryDate())
    );
    outStr.append(
        String.format(":Issue Date: %s%n", this.getIssueDate())
    );
    outStr.append(
        String.format(":Issue Authority: %s%n", this.getIssueAuthority())
    );
    outStr.append(
        String.format(":MRZ: %s%n", this.getMrz())
    );
    outStr.append(
        String.format(":Address: %s%n", this.getAddress())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
