package com.mindee.parsing.fr.bankaccountdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
* Document data for Bank Account Details, API version 1.
*/
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountDetailsV1DocumentPrediction {

  /**
  * The International Bank Account Number (IBAN).
  */
  @JsonProperty("iban")
  private StringField iban;

  /**
  * The name of the account holder as seen on the document.
  */
  @JsonProperty("account_holder_name")
  private StringField accountHolderName;

  /**
  * The bank's SWIFT Business Identifier Code (BIC).
  */
  @JsonProperty("swift")
  private StringField swift;

  @Override
  public String toString() {
    String summary =
        String.format(":IBAN: %s%n", this.getIban())
        + String.format(":Account Holder's Name: %s%n", this.getAccountHolderName())
        + String.format(":SWIFT Code: %s%n", this.getSwift());
    return SummaryHelper.cleanSummary(summary);
  }
}
