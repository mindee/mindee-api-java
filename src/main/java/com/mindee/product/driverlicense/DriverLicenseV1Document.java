package com.mindee.product.driverlicense;

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
   * The category or class of the driver license.
   */
  @JsonProperty("category")
  protected StringField category;
  /**
   * The alpha-3 ISO 3166 code of the country where the driver license was issued.
   */
  @JsonProperty("country_code")
  protected StringField countryCode;
  /**
   * The date of birth of the driver license holder.
   */
  @JsonProperty("date_of_birth")
  protected DateField dateOfBirth;
  /**
   * The DD number of the driver license.
   */
  @JsonProperty("dd_number")
  protected StringField ddNumber;
  /**
   * The expiry date of the driver license.
   */
  @JsonProperty("expiry_date")
  protected DateField expiryDate;
  /**
   * The first name of the driver license holder.
   */
  @JsonProperty("first_name")
  protected StringField firstName;
  /**
   * The unique identifier of the driver license.
   */
  @JsonProperty("id")
  protected StringField id;
  /**
   * The date when the driver license was issued.
   */
  @JsonProperty("issued_date")
  protected DateField issuedDate;
  /**
   * The authority that issued the driver license.
   */
  @JsonProperty("issuing_authority")
  protected StringField issuingAuthority;
  /**
   * The last name of the driver license holder.
   */
  @JsonProperty("last_name")
  protected StringField lastName;
  /**
   * The Machine Readable Zone (MRZ) of the driver license.
   */
  @JsonProperty("mrz")
  protected StringField mrz;
  /**
   * The place of birth of the driver license holder.
   */
  @JsonProperty("place_of_birth")
  protected StringField placeOfBirth;
  /**
   * Second part of the ISO 3166-2 code, consisting of two letters indicating the US State.
   */
  @JsonProperty("state")
  protected StringField state;

  @Override
  public boolean isEmpty() {
    return (
      this.countryCode == null
      && this.state == null
      && this.id == null
      && this.category == null
      && this.lastName == null
      && this.firstName == null
      && this.dateOfBirth == null
      && this.placeOfBirth == null
      && this.expiryDate == null
      && this.issuedDate == null
      && this.issuingAuthority == null
      && this.mrz == null
      && this.ddNumber == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Country Code: %s%n", this.getCountryCode())
    );
    outStr.append(
        String.format(":State: %s%n", this.getState())
    );
    outStr.append(
        String.format(":ID: %s%n", this.getId())
    );
    outStr.append(
        String.format(":Category: %s%n", this.getCategory())
    );
    outStr.append(
        String.format(":Last Name: %s%n", this.getLastName())
    );
    outStr.append(
        String.format(":First Name: %s%n", this.getFirstName())
    );
    outStr.append(
        String.format(":Date of Birth: %s%n", this.getDateOfBirth())
    );
    outStr.append(
        String.format(":Place of Birth: %s%n", this.getPlaceOfBirth())
    );
    outStr.append(
        String.format(":Expiry Date: %s%n", this.getExpiryDate())
    );
    outStr.append(
        String.format(":Issued Date: %s%n", this.getIssuedDate())
    );
    outStr.append(
        String.format(":Issuing Authority: %s%n", this.getIssuingAuthority())
    );
    outStr.append(
        String.format(":MRZ: %s%n", this.getMrz())
    );
    outStr.append(
        String.format(":DD Number: %s%n", this.getDdNumber())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
