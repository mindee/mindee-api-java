package com.mindee.product.us.driverlicense;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Driver License API version 1.1 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverLicenseV1Document extends Prediction {

  /**
   * US driver license holders address
   */
  @JsonProperty("address")
  protected StringField address;
  /**
   * US driver license holders date of birth
   */
  @JsonProperty("date_of_birth")
  protected DateField dateOfBirth;
  /**
   * Document Discriminator Number of the US Driver License
   */
  @JsonProperty("dd_number")
  protected StringField ddNumber;
  /**
   * US driver license holders class
   */
  @JsonProperty("dl_class")
  protected StringField dlClass;
  /**
   * ID number of the US Driver License.
   */
  @JsonProperty("driver_license_id")
  protected StringField driverLicenseId;
  /**
   * US driver license holders endorsements
   */
  @JsonProperty("endorsements")
  protected StringField endorsements;
  /**
   * Date on which the documents expires.
   */
  @JsonProperty("expiry_date")
  protected DateField expiryDate;
  /**
   * US driver license holders eye colour
   */
  @JsonProperty("eye_color")
  protected StringField eyeColor;
  /**
   * US driver license holders first name(s)
   */
  @JsonProperty("first_name")
  protected StringField firstName;
  /**
   * US driver license holders hair colour
   */
  @JsonProperty("hair_color")
  protected StringField hairColor;
  /**
   * US driver license holders hight
   */
  @JsonProperty("height")
  protected StringField height;
  /**
   * Date on which the documents was issued.
   */
  @JsonProperty("issued_date")
  protected DateField issuedDate;
  /**
   * US driver license holders last name
   */
  @JsonProperty("last_name")
  protected StringField lastName;
  /**
   * US driver license holders restrictions
   */
  @JsonProperty("restrictions")
  protected StringField restrictions;
  /**
   * US driver license holders gender
   */
  @JsonProperty("sex")
  protected StringField sex;
  /**
   * US State
   */
  @JsonProperty("state")
  protected StringField state;
  /**
   * US driver license holders weight
   */
  @JsonProperty("weight")
  protected StringField weight;

  @Override
  public boolean isEmpty() {
    return (
      this.state == null
      && this.driverLicenseId == null
      && this.expiryDate == null
      && this.issuedDate == null
      && this.lastName == null
      && this.firstName == null
      && this.address == null
      && this.dateOfBirth == null
      && this.restrictions == null
      && this.endorsements == null
      && this.dlClass == null
      && this.sex == null
      && this.height == null
      && this.weight == null
      && this.hairColor == null
      && this.eyeColor == null
      && this.ddNumber == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":State: %s%n", this.getState())
    );
    outStr.append(
        String.format(":Driver License ID: %s%n", this.getDriverLicenseId())
    );
    outStr.append(
        String.format(":Expiry Date: %s%n", this.getExpiryDate())
    );
    outStr.append(
        String.format(":Date Of Issue: %s%n", this.getIssuedDate())
    );
    outStr.append(
        String.format(":Last Name: %s%n", this.getLastName())
    );
    outStr.append(
        String.format(":First Name: %s%n", this.getFirstName())
    );
    outStr.append(
        String.format(":Address: %s%n", this.getAddress())
    );
    outStr.append(
        String.format(":Date Of Birth: %s%n", this.getDateOfBirth())
    );
    outStr.append(
        String.format(":Restrictions: %s%n", this.getRestrictions())
    );
    outStr.append(
        String.format(":Endorsements: %s%n", this.getEndorsements())
    );
    outStr.append(
        String.format(":Driver License Class: %s%n", this.getDlClass())
    );
    outStr.append(
        String.format(":Sex: %s%n", this.getSex())
    );
    outStr.append(
        String.format(":Height: %s%n", this.getHeight())
    );
    outStr.append(
        String.format(":Weight: %s%n", this.getWeight())
    );
    outStr.append(
        String.format(":Hair Color: %s%n", this.getHairColor())
    );
    outStr.append(
        String.format(":Eye Color: %s%n", this.getEyeColor())
    );
    outStr.append(
        String.format(":Document Discriminator: %s%n", this.getDdNumber())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
