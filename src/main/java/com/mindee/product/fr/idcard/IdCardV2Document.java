package com.mindee.product.fr.idcard;

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
 * Carte Nationale d'Identité API version 2.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdCardV2Document extends Prediction {

  /**
   * The alternate name of the card holder.
   */
  @JsonProperty("alternate_name")
  protected StringField alternateName;
  /**
   * The name of the issuing authority.
   */
  @JsonProperty("authority")
  protected StringField authority;
  /**
   * The date of birth of the card holder.
   */
  @JsonProperty("birth_date")
  protected DateField birthDate;
  /**
   * The place of birth of the card holder.
   */
  @JsonProperty("birth_place")
  protected StringField birthPlace;
  /**
   * The card access number (CAN).
   */
  @JsonProperty("card_access_number")
  protected StringField cardAccessNumber;
  /**
   * The document number.
   */
  @JsonProperty("document_number")
  protected StringField documentNumber;
  /**
   * The expiry date of the identification card.
   */
  @JsonProperty("expiry_date")
  protected DateField expiryDate;
  /**
   * The gender of the card holder.
   */
  @JsonProperty("gender")
  protected StringField gender;
  /**
   * The given name(s) of the card holder.
   */
  @JsonProperty("given_names")
  protected List<StringField> givenNames = new ArrayList<>();
  /**
   * The date of issue of the identification card.
   */
  @JsonProperty("issue_date")
  protected DateField issueDate;
  /**
   * The Machine Readable Zone, first line.
   */
  @JsonProperty("mrz1")
  protected StringField mrz1;
  /**
   * The Machine Readable Zone, second line.
   */
  @JsonProperty("mrz2")
  protected StringField mrz2;
  /**
   * The Machine Readable Zone, third line.
   */
  @JsonProperty("mrz3")
  protected StringField mrz3;
  /**
   * The nationality of the card holder.
   */
  @JsonProperty("nationality")
  protected StringField nationality;
  /**
   * The surname of the card holder.
   */
  @JsonProperty("surname")
  protected StringField surname;

  @Override
  public boolean isEmpty() {
    return (this.nationality == null
      && this.cardAccessNumber == null
      && this.documentNumber == null
      && (this.givenNames == null || this.givenNames.isEmpty())
      && this.surname == null
      && this.alternateName == null
      && this.birthDate == null
      && this.birthPlace == null
      && this.gender == null
      && this.expiryDate == null
      && this.mrz1 == null
      && this.mrz2 == null
      && this.mrz3 == null
      && this.issueDate == null
      && this.authority == null);
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(String.format(":Nationality: %s%n", this.getNationality()));
    outStr.append(String.format(":Card Access Number: %s%n", this.getCardAccessNumber()));
    outStr.append(String.format(":Document Number: %s%n", this.getDocumentNumber()));
    String givenNames = SummaryHelper.arrayToString(this.getGivenNames(), "%n                ");
    outStr.append(String.format(":Given Name(s): %s%n", givenNames));
    outStr.append(String.format(":Surname: %s%n", this.getSurname()));
    outStr.append(String.format(":Alternate Name: %s%n", this.getAlternateName()));
    outStr.append(String.format(":Date of Birth: %s%n", this.getBirthDate()));
    outStr.append(String.format(":Place of Birth: %s%n", this.getBirthPlace()));
    outStr.append(String.format(":Gender: %s%n", this.getGender()));
    outStr.append(String.format(":Expiry Date: %s%n", this.getExpiryDate()));
    outStr.append(String.format(":Mrz Line 1: %s%n", this.getMrz1()));
    outStr.append(String.format(":Mrz Line 2: %s%n", this.getMrz2()));
    outStr.append(String.format(":Mrz Line 3: %s%n", this.getMrz3()));
    outStr.append(String.format(":Date of Issue: %s%n", this.getIssueDate()));
    outStr.append(String.format(":Issuing Authority: %s%n", this.getAuthority()));
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
