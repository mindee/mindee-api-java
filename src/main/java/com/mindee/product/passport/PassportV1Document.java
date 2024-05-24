package com.mindee.product.passport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Passport API version 1.1 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PassportV1Document extends Prediction {

  /**
   * The date of birth of the passport holder.
   */
  @JsonProperty("birth_date")
  protected DateField birthDate;
  /**
   * The place of birth of the passport holder.
   */
  @JsonProperty("birth_place")
  protected StringField birthPlace;
  /**
   * The country's 3 letter code (ISO 3166-1 alpha-3).
   */
  @JsonProperty("country")
  protected StringField country;
  /**
   * The expiry date of the passport.
   */
  @JsonProperty("expiry_date")
  protected DateField expiryDate;
  /**
   * The gender of the passport holder.
   */
  @JsonProperty("gender")
  protected StringField gender;
  /**
   * The given name(s) of the passport holder.
   */
  @JsonProperty("given_names")
  protected List<StringField> givenNames = new ArrayList<>();
  /**
   * The passport's identification number.
   */
  @JsonProperty("id_number")
  protected StringField idNumber;
  /**
   * The date the passport was issued.
   */
  @JsonProperty("issuance_date")
  protected DateField issuanceDate;
  /**
   * Machine Readable Zone, first line
   */
  @JsonProperty("mrz1")
  protected StringField mrz1;
  /**
   * Machine Readable Zone, second line
   */
  @JsonProperty("mrz2")
  protected StringField mrz2;
  /**
   * The surname of the passport holder.
   */
  @JsonProperty("surname")
  protected StringField surname;

  @Override
  public boolean isEmpty() {
    return (
      this.country == null
      && this.idNumber == null
      && (this.givenNames == null || this.givenNames.isEmpty())
      && this.surname == null
      && this.birthDate == null
      && this.birthPlace == null
      && this.gender == null
      && this.issuanceDate == null
      && this.expiryDate == null
      && this.mrz1 == null
      && this.mrz2 == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Country Code: %s%n", this.getCountry())
    );
    outStr.append(
        String.format(":ID Number: %s%n", this.getIdNumber())
    );
    String givenNames = SummaryHelper.arrayToString(
        this.getGivenNames(),
        "%n                "
    );
    outStr.append(
        String.format(":Given Name(s): %s%n", givenNames)
    );
    outStr.append(
        String.format(":Surname: %s%n", this.getSurname())
    );
    outStr.append(
        String.format(":Date of Birth: %s%n", this.getBirthDate())
    );
    outStr.append(
        String.format(":Place of Birth: %s%n", this.getBirthPlace())
    );
    outStr.append(
        String.format(":Gender: %s%n", this.getGender())
    );
    outStr.append(
        String.format(":Date of Issue: %s%n", this.getIssuanceDate())
    );
    outStr.append(
        String.format(":Expiry Date: %s%n", this.getExpiryDate())
    );
    outStr.append(
        String.format(":MRZ Line 1: %s%n", this.getMrz1())
    );
    outStr.append(
        String.format(":MRZ Line 2: %s%n", this.getMrz2())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
