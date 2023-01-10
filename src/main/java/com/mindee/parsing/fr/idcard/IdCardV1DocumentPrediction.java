package com.mindee.parsing.fr.idcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.DateField;
import com.mindee.parsing.common.field.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdCardV1DocumentPrediction {

  /**
   * The authority which has issued the card.
   */
  @JsonProperty("authority")
  private StringField authority;
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
   * The expiry date of the card.
   */
  @JsonProperty("expiry_date")
  private DateField expiryDate;
  /**
   * The gender of the person.
   */
  @JsonProperty("gender")
  private StringField gender;
  /**
   * The list of the names of the person.
   */
  @JsonProperty("given_names")
  private List<StringField> givenNames = new ArrayList<>();
  /**
   * The id number of the card.
   */
  @JsonProperty("id_number")
  private StringField idNumber;
  /**
   * The first mrz value.
   */
  @JsonProperty("mrz1")
  private StringField mrz1;
  /**
   * The second mrz value.
   */
  @JsonProperty("mrz2")
  private StringField mrz2;
  /**
   * The surname of the person.
   */
  @JsonProperty("surname")
  private StringField surname;

  @Override
  public String toString() {

    String summary =
      String.format(":Authority: %s%n", this.getAuthority()) +
        String.format(":Given names: %s%n",
          this.getGivenNames().stream()
          .map(StringField::toString)
          .collect(Collectors.joining(", "))) +
        String.format(":Surname: %s%n", this.getSurname()) +
        String.format(":Gender: %s%n", this.getGender()) +
        String.format(":ID Number: %s%n", this.getIdNumber()) +
        String.format(":Birth date: %s%n", this.getBirthDate()) +
        String.format(":Birth place: %s%n", this.getBirthPlace()) +
        String.format(":Expiry date: %s%n", this.getExpiryDate()) +
        String.format(":MRZ 1: %s%n", this.getMrz1()) +
        String.format(":MRZ 2: %s%n", this.getMrz2());

    return SummaryHelper.cleanSummary(summary);
  }
}
