package com.mindee.product.fr.healthcard;

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
 * Health Card API version 1.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthCardV1Document extends Prediction {

  /**
   * The given names of the card holder.
   */
  @JsonProperty("given_names")
  protected List<StringField> givenNames = new ArrayList<>();
  /**
   * The date when the carte vitale document was issued.
   */
  @JsonProperty("issuance_date")
  protected DateField issuanceDate;
  /**
   * The social security number of the card holder.
   */
  @JsonProperty("social_security")
  protected StringField socialSecurity;
  /**
   * The surname of the card holder.
   */
  @JsonProperty("surname")
  protected StringField surname;

  @Override
  public boolean isEmpty() {
    return (
      (this.givenNames == null || this.givenNames.isEmpty())
      && this.surname == null
      && this.socialSecurity == null
      && this.issuanceDate == null
      );
  }

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
