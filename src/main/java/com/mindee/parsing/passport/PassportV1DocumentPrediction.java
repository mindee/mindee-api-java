package com.mindee.parsing.passport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.DateField;
import com.mindee.parsing.common.field.StringField;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Passport, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class PassportV1DocumentPrediction {
  /**
   * The country of issue.
   */
  @JsonProperty("country")
  private StringField country;

  /**
   * The passport number.
   */
  @JsonProperty("id_number")
  private StringField idNumber;

  /**
   * The birth date of the person.
   */
  @JsonProperty("birth_date")
  private DateField birthDate;

  /**
   * The birth place of the person.
   */
  @JsonProperty("birth_place")
  private StringField birthPlace;

  /**
   * The expiry date.
   */
  @JsonProperty("expiry_date")
  private DateField expiryDate;

  /**
   * The gender of the person.
   */
  @JsonProperty("gender")
  private StringField gender;

  /**
   * The list of the person given names.
   */
  @JsonProperty("given_names")
  private List<StringField> givenNames;

  /**
   * The surname of the person.
   */
  @JsonProperty("surname")
  private StringField surname;

  /**
   * The date of issuance of the passport.
   */
  @JsonProperty("issuance_date")
  private DateField issuanceDate;

  /**
   * The first MRZ line.
   */
  @JsonProperty("mrz1")
  private StringField mrz1;

  /**
   * The second MRZ line.
   */
  @JsonProperty("mrz2")
  private StringField mrz2;

  /**
   * Combine the MRZ lines.
   */
  public String getMrz() {
    return mrz1.getValue() + mrz2.getValue();
  }

  /**
   * Get the full name of the person.
   */
  public String getFullName() {
    return this.givenNames.stream()
      .map(StringField::toString)
      .collect(Collectors.joining(" "))
      + " " + this.surname;
  }

  @Override
  public String toString() {

    String summary =
        String.format(":Full name: %s%n", this.getFullName())
        + String.format(":Given names: %s%n",
          this.getGivenNames().stream()
            .map(StringField::toString)
            .collect(Collectors.joining(" ")))
        + String.format(":Surname: %s%n", this.getSurname())
        + String.format(":Country: %s%n", this.getCountry())
        + String.format(":ID Number: %s%n", this.getIdNumber())
        + String.format(":Issuance date: %s%n", this.getIssuanceDate())
        + String.format(":Birth date: %s%n", this.getBirthDate())
        + String.format(":Expiry date: %s%n", this.getExpiryDate())
        + String.format(":MRZ 1: %s%n", this.getMrz1())
        + String.format(":MRZ 2: %s%n", this.getMrz2())
        + String.format(":MRZ: %s%n", this.getMrz());

    return SummaryHelper.cleanSummary(summary);
  }
}
