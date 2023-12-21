package com.mindee.product.fr.bankaccountdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Bank Account Details, API version 1.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountDetailsV1Document extends Prediction {

  /**
   * The name of the account holder as seen on the document.
   */
  @JsonProperty("account_holder_name")
  private StringField accountHolderName;
  /**
   * The International Bank Account Number (IBAN).
   */
  @JsonProperty("iban")
  private StringField iban;
  /**
   * The bank's SWIFT Business Identifier Code (BIC).
   */
  @JsonProperty("swift")
  private StringField swift;

  @Override
  public boolean isEmpty() {
    return (
      this.iban == null
      && this.accountHolderName == null
      && this.swift == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":IBAN: %s%n", this.getIban())
    );
    outStr.append(
        String.format(":Account Holder's Name: %s%n", this.getAccountHolderName())
    );
    outStr.append(
        String.format(":SWIFT Code: %s%n", this.getSwift())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
