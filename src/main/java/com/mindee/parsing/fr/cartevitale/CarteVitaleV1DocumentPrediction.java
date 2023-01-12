package com.mindee.parsing.fr.cartevitale;

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
public class CarteVitaleV1DocumentPrediction {

  /**
   * The list of the names of the person.
   */
  @JsonProperty("given_names")
  private List<StringField> givenNames = new ArrayList<>();
  /**
   * The surname of the person.
   */
  @JsonProperty("surname")
  private StringField surname;
  /**
   * The social security number.
   */
  @JsonProperty("social_security")
  private StringField socialSecurityNumber;
  /**
   * Date the card  was issued.
   */
  @JsonProperty("issuance_date")
  private DateField issuanceDate;

  @Override
  public String toString() {

    String summary =
        String.format(":Given names: %s%n",
          this.getGivenNames().stream()
          .map(StringField::toString)
          .collect(Collectors.joining(", "))) +
        String.format(":Surname: %s%n", this.getSurname()) +
        String.format(":Social Security Number: %s%n", this.getSocialSecurityNumber()) +
        String.format(":Issuance date: %s%n", this.getIssuanceDate());

    return SummaryHelper.cleanSummary(summary);
  }
}
