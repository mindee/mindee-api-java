package com.mindee.product.us.bankcheck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Bank Check, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankCheckV1Document {

  /**
   * The check payer's account number.
   */
  @JsonProperty("account_number")
  private StringField accountNumber;
  /**
   * The amount of the check.
   */
  @JsonProperty("amount")
  private AmountField amount;
  /**
   * The issuer's check number.
   */
  @JsonProperty("check_number")
  private StringField checkNumber;
  /**
   * The date the check was issued.
   */
  @JsonProperty("date")
  private DateField date;
  /**
   * List of the check's payees (recipients).
   */
  @JsonProperty("payees")
  private List<StringField> payees = new ArrayList<>();
  /**
   * The check issuer's routing number.
   */
  @JsonProperty("routing_number")
  private StringField routingNumber;

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Check Issue Date: %s%n", this.getDate())
    );
    outStr.append(
        String.format(":Amount: %s%n", this.getAmount())
    );
    String payees = SummaryHelper.arrayToString(
        this.getPayees(),
        "%n         "
    );
    outStr.append(
        String.format(":Payees: %s%n", payees)
    );
    outStr.append(
        String.format(":Routing Number: %s%n", this.getRoutingNumber())
    );
    outStr.append(
        String.format(":Account Number: %s%n", this.getAccountNumber())
    );
    outStr.append(
        String.format(":Check Number: %s%n", this.getCheckNumber())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
