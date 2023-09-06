package com.mindee.product.fr.cartevitale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
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
public class CarteVitaleV1Document {

  /**
   * The given name(s) of the card holder.
   */
  @JsonProperty("given_names")
  private List<StringField> givenNames = new ArrayList<>();
  /**
   * The date the card was issued.
   */
  @JsonProperty("issuance_date")
  private DateField issuanceDate;
  /**
   * The Social Security Number (Numéro de Sécurité Sociale) of the card holder
   */
  @JsonProperty("social_security")
  private StringField socialSecurity;
  /**
   * The surname of the card holder.
   */
  @JsonProperty("surname")
  private StringField surname;

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
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
        String.format(":Social Security Number: %s%n", this.getSocialSecurity())
    );
    outStr.append(
        String.format(":Issuance Date: %s%n", this.getIssuanceDate())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
