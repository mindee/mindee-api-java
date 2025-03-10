package com.mindee.product.ind.indianpassport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Passport - India API version 1.2 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndianPassportV1Document extends Prediction {

  /**
   * The first line of the address of the passport holder.
   */
  @JsonProperty("address1")
  protected StringField address1;
  /**
   * The second line of the address of the passport holder.
   */
  @JsonProperty("address2")
  protected StringField address2;
  /**
   * The third line of the address of the passport holder.
   */
  @JsonProperty("address3")
  protected StringField address3;
  /**
   * The birth date of the passport holder, ISO format: YYYY-MM-DD.
   */
  @JsonProperty("birth_date")
  protected DateField birthDate;
  /**
   * The birth place of the passport holder.
   */
  @JsonProperty("birth_place")
  protected StringField birthPlace;
  /**
   * ISO 3166-1 alpha-3 country code (3 letters format).
   */
  @JsonProperty("country")
  protected StringField country;
  /**
   * The date when the passport will expire, ISO format: YYYY-MM-DD.
   */
  @JsonProperty("expiry_date")
  protected DateField expiryDate;
  /**
   * The file number of the passport document.
   */
  @JsonProperty("file_number")
  protected StringField fileNumber;
  /**
   * The gender of the passport holder.
   */
  @JsonProperty("gender")
  protected ClassificationField gender;
  /**
   * The given names of the passport holder.
   */
  @JsonProperty("given_names")
  protected StringField givenNames;
  /**
   * The identification number of the passport document.
   */
  @JsonProperty("id_number")
  protected StringField idNumber;
  /**
   * The date when the passport was issued, ISO format: YYYY-MM-DD.
   */
  @JsonProperty("issuance_date")
  protected DateField issuanceDate;
  /**
   * The place where the passport was issued.
   */
  @JsonProperty("issuance_place")
  protected StringField issuancePlace;
  /**
   * The name of the legal guardian of the passport holder (if applicable).
   */
  @JsonProperty("legal_guardian")
  protected StringField legalGuardian;
  /**
   * The first line of the machine-readable zone (MRZ) of the passport document.
   */
  @JsonProperty("mrz1")
  protected StringField mrz1;
  /**
   * The second line of the machine-readable zone (MRZ) of the passport document.
   */
  @JsonProperty("mrz2")
  protected StringField mrz2;
  /**
   * The name of the mother of the passport holder.
   */
  @JsonProperty("name_of_mother")
  protected StringField nameOfMother;
  /**
   * The name of the spouse of the passport holder (if applicable).
   */
  @JsonProperty("name_of_spouse")
  protected StringField nameOfSpouse;
  /**
   * The date of issue of the old passport (if applicable), ISO format: YYYY-MM-DD.
   */
  @JsonProperty("old_passport_date_of_issue")
  protected DateField oldPassportDateOfIssue;
  /**
   * The number of the old passport (if applicable).
   */
  @JsonProperty("old_passport_number")
  protected StringField oldPassportNumber;
  /**
   * The place of issue of the old passport (if applicable).
   */
  @JsonProperty("old_passport_place_of_issue")
  protected StringField oldPassportPlaceOfIssue;
  /**
   * The page number of the passport document.
   */
  @JsonProperty("page_number")
  protected ClassificationField pageNumber;
  /**
   * The surname of the passport holder.
   */
  @JsonProperty("surname")
  protected StringField surname;

  @Override
  public boolean isEmpty() {
    return (
      this.pageNumber == null
      && this.country == null
      && this.idNumber == null
      && this.givenNames == null
      && this.surname == null
      && this.birthDate == null
      && this.birthPlace == null
      && this.issuancePlace == null
      && this.gender == null
      && this.issuanceDate == null
      && this.expiryDate == null
      && this.mrz1 == null
      && this.mrz2 == null
      && this.legalGuardian == null
      && this.nameOfSpouse == null
      && this.nameOfMother == null
      && this.oldPassportDateOfIssue == null
      && this.oldPassportNumber == null
      && this.address1 == null
      && this.address2 == null
      && this.address3 == null
      && this.oldPassportPlaceOfIssue == null
      && this.fileNumber == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Page Number: %s%n", this.getPageNumber())
    );
    outStr.append(
        String.format(":Country: %s%n", this.getCountry())
    );
    outStr.append(
        String.format(":ID Number: %s%n", this.getIdNumber())
    );
    outStr.append(
        String.format(":Given Names: %s%n", this.getGivenNames())
    );
    outStr.append(
        String.format(":Surname: %s%n", this.getSurname())
    );
    outStr.append(
        String.format(":Birth Date: %s%n", this.getBirthDate())
    );
    outStr.append(
        String.format(":Birth Place: %s%n", this.getBirthPlace())
    );
    outStr.append(
        String.format(":Issuance Place: %s%n", this.getIssuancePlace())
    );
    outStr.append(
        String.format(":Gender: %s%n", this.getGender())
    );
    outStr.append(
        String.format(":Issuance Date: %s%n", this.getIssuanceDate())
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
    outStr.append(
        String.format(":Legal Guardian: %s%n", this.getLegalGuardian())
    );
    outStr.append(
        String.format(":Name of Spouse: %s%n", this.getNameOfSpouse())
    );
    outStr.append(
        String.format(":Name of Mother: %s%n", this.getNameOfMother())
    );
    outStr.append(
        String.format(":Old Passport Date of Issue: %s%n", this.getOldPassportDateOfIssue())
    );
    outStr.append(
        String.format(":Old Passport Number: %s%n", this.getOldPassportNumber())
    );
    outStr.append(
        String.format(":Address Line 1: %s%n", this.getAddress1())
    );
    outStr.append(
        String.format(":Address Line 2: %s%n", this.getAddress2())
    );
    outStr.append(
        String.format(":Address Line 3: %s%n", this.getAddress3())
    );
    outStr.append(
        String.format(":Old Passport Place of Issue: %s%n", this.getOldPassportPlaceOfIssue())
    );
    outStr.append(
        String.format(":File Number: %s%n", this.getFileNumber())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
