package com.mindee.product.us.driverlicense;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Driver License, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverLicenseV1Document {

  /**
   * US driver license holders address
   */
  @JsonProperty("address")
  private StringField address;
  /**
   * US driver license holders date of birth
   */
  @JsonProperty("date_of_birth")
  private DateField dateOfBirth;
  /**
   * Document Discriminator Number of the US Driver License
   */
  @JsonProperty("dd_number")
  private StringField ddNumber;
  /**
   * US driver license holders class
   */
  @JsonProperty("dl_class")
  private StringField dlClass;
  /**
   * ID number of the US Driver License.
   */
  @JsonProperty("driver_license_id")
  private StringField driverLicenseId;
  /**
   * US driver license holders endorsements
   */
  @JsonProperty("endorsements")
  private StringField endorsements;
  /**
   * Date on which the documents expires.
   */
  @JsonProperty("expiry_date")
  private DateField expiryDate;
  /**
   * US driver license holders eye colour
   */
  @JsonProperty("eye_color")
  private StringField eyeColor;
  /**
   * US driver license holders first name(s)
   */
  @JsonProperty("first_name")
  private StringField firstName;
  /**
   * US driver license holders hair colour
   */
  @JsonProperty("hair_color")
  private StringField hairColor;
  /**
   * US driver license holders hight
   */
  @JsonProperty("height")
  private StringField height;
  /**
   * Date on which the documents was issued.
   */
  @JsonProperty("issued_date")
  private DateField issuedDate;
  /**
   * US driver license holders last name
   */
  @JsonProperty("last_name")
  private StringField lastName;
  /**
   * US driver license holders restrictions
   */
  @JsonProperty("restrictions")
  private StringField restrictions;
  /**
   * US driver license holders gender
   */
  @JsonProperty("sex")
  private StringField sex;
  /**
   * US State
   */
  @JsonProperty("state")
  private StringField state;
  /**
   * US driver license holders weight
   */
  @JsonProperty("weight")
  private StringField weight;

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
