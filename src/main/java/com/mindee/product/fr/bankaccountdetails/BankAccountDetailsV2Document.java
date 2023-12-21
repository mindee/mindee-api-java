package com.mindee.product.fr.bankaccountdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Bank Account Details, API version 2.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountDetailsV2Document extends Prediction {

  /**
   * Full extraction of the account holders names.
   */
  @JsonProperty("account_holders_names")
  private StringField accountHoldersNames;
  /**
   * Full extraction of BBAN, including: branch code, bank code, account and key.
   */
  @JsonProperty("bban")
  private BankAccountDetailsV2Bban bban;
  /**
   * Full extraction of the IBAN number.
   */
  @JsonProperty("iban")
  private StringField iban;
  /**
   * Full extraction of the SWIFT code.
   */
  @JsonProperty("swift_code")
  private StringField swiftCode;

  @Override
  public boolean isEmpty() {
    return (
      this.accountHoldersNames == null
      && this.bban == null
      && this.iban == null
      && this.swiftCode == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Account Holder's Names: %s%n", this.getAccountHoldersNames())
    );
    outStr.append(
        String.format(":Basic Bank Account Number:%n%s", this.getBban().toFieldList())
    );
    outStr.append(
        String.format(":IBAN: %s%n", this.getIban())
    );
    outStr.append(
        String.format(":SWIFT Code: %s%n", this.getSwiftCode())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
