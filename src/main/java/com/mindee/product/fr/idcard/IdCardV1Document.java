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
 * Carte Nationale d'Identité API version 1.1 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdCardV1Document extends Prediction {

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
   * The identification card number.
   */
  @JsonProperty("id_number")
  protected StringField idNumber;
  /**
   * Machine Readable Zone, first line.
   */
  @JsonProperty("mrz1")
  protected StringField mrz1;
  /**
   * Machine Readable Zone, second line.
   */
  @JsonProperty("mrz2")
  protected StringField mrz2;
  /**
   * The surname of the card holder.
   */
  @JsonProperty("surname")
  protected StringField surname;

  @Override
  public boolean isEmpty() {
    return (this.idNumber == null
      && (this.givenNames == null || this.givenNames.isEmpty())
      && this.surname == null
      && this.birthDate == null
      && this.birthPlace == null
      && this.expiryDate == null
      && this.authority == null
      && this.gender == null
      && this.mrz1 == null
      && this.mrz2 == null);
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(String.format(":Identity Number: %s%n", this.getIdNumber()));
    String givenNames = SummaryHelper.arrayToString(this.getGivenNames(), "%n                ");
    outStr.append(String.format(":Given Name(s): %s%n", givenNames));
    outStr.append(String.format(":Surname: %s%n", this.getSurname()));
    outStr.append(String.format(":Date of Birth: %s%n", this.getBirthDate()));
    outStr.append(String.format(":Place of Birth: %s%n", this.getBirthPlace()));
    outStr.append(String.format(":Expiry Date: %s%n", this.getExpiryDate()));
    outStr.append(String.format(":Issuing Authority: %s%n", this.getAuthority()));
    outStr.append(String.format(":Gender: %s%n", this.getGender()));
    outStr.append(String.format(":MRZ Line 1: %s%n", this.getMrz1()));
    outStr.append(String.format(":MRZ Line 2: %s%n", this.getMrz2()));
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
