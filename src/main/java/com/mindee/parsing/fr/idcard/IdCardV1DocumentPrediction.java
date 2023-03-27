package com.mindee.parsing.fr.idcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.DateField;
import com.mindee.parsing.common.field.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
* Document data for Carte Nationale d'Identité, API version 1.
*/
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdCardV1DocumentPrediction {

  /**
  * The identification card number.
  */
  @JsonProperty("id_number")
  private StringField idNumber;

  /**
  * The given name(s) of the card holder.
  */
  @JsonProperty("given_names")
  private List<StringField> givenNames = new ArrayList<>();

  /**
  * The surname of the card holder.
  */
  @JsonProperty("surname")
  private StringField surname;

  /**
  * The date of birth of the card holder.
  */
  @JsonProperty("birth_date")
  private DateField birthDate;

  /**
  * The place of birth of the card holder.
  */
  @JsonProperty("birth_place")
  private StringField birthPlace;

  /**
  * The expiry date of the identification card.
  */
  @JsonProperty("expiry_date")
  private DateField expiryDate;

  /**
  * The name of the issuing authority.
  */
  @JsonProperty("authority")
  private StringField authority;

  /**
  * The gender of the card holder.
  */
  @JsonProperty("gender")
  private StringField gender;

  /**
  * Machine Readable Zone, first line
  */
  @JsonProperty("mrz1")
  private StringField mrz1;

  /**
  * Machine Readable Zone, second line
  */
  @JsonProperty("mrz2")
  private StringField mrz2;

  @Override
  public String toString() {
    String summary =
        String.format(":Identity Number: %s%n", this.getIdNumber())
        + String.format(
          ":Given Name(s): %s%n",
          SummaryHelper.arrayToString(
            this.getGivenNames(),
            "%n              "
          )
        )
        + String.format(":Surname: %s%n", this.getSurname())
        + String.format(":Date of Birth: %s%n", this.getBirthDate())
        + String.format(":Place of Birth: %s%n", this.getBirthPlace())
        + String.format(":Expiry Date: %s%n", this.getExpiryDate())
        + String.format(":Issuing Authority: %s%n", this.getAuthority())
        + String.format(":Gender: %s%n", this.getGender())
        + String.format(":MRZ Line 1: %s%n", this.getMrz1())
        + String.format(":MRZ Line 2: %s%n", this.getMrz2());
    return SummaryHelper.cleanSummary(summary);
  }
}
