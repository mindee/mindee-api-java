package com.mindee.parsing.fr.cartevitale;

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
* Document data for Carte Vitale, API version 1.
*/
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarteVitaleV1DocumentPrediction {

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
  * The Social Security Number (Numéro de Sécurité Sociale) of the card holder
  */
  @JsonProperty("social_security")
  private StringField socialSecurity;

  /**
  * The date the card was issued.
  */
  @JsonProperty("issuance_date")
  private DateField issuanceDate;

  @Override
  public String toString() {
    String summary =
        String.format(
          ":Given Name(s): %s%n",
          SummaryHelper.arrayToString(
            this.getGivenNames(),
            "%n              "
          )
        )
        + String.format(":Surname: %s%n", this.getSurname())
        + String.format(":Social Security Number: %s%n", this.getSocialSecurity())
        + String.format(":Issuance Date: %s%n", this.getIssuanceDate());
    return SummaryHelper.cleanSummary(summary);
  }
}
