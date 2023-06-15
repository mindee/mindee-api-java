package com.mindee.product.us.bankcheck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.field.AmountField;
import com.mindee.parsing.standard.field.DateField;
import com.mindee.parsing.standard.field.PositionField;
import com.mindee.parsing.standard.field.StringField;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for US Bank Check, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankCheckV1Document {

  /**
   * Payer's bank account number.
   */
  @JsonProperty("account_number")
  private StringField accountNumber;
  /**
   * Total amount.
   */
  @JsonProperty("amount")
  private AmountField amount;
  /**
   * The check number.
   */
  @JsonProperty("check_number")
  private StringField checkNumber;
  /**
   * Check's position in the image.
   */
  @JsonProperty("check_position")
  private PositionField checkPosition;
  /**
   * Date the check was issued.
   */
  @JsonProperty("date")
  private DateField date;
  /**
   * List of payees (full name or company name).
   */
  @JsonProperty("payees")
  private List<StringField> payees = new ArrayList<>();
  /**
   * Payer's bank account routing number.
   */
  @JsonProperty("routing_number")
  private StringField routingNumber;
  /**
   * The positions of the signatures on the image.
   */
  @JsonProperty("signatures_positions")
  private List<PositionField> signaturesPositions = new ArrayList<>();

  @Override
  public String toString() {

    String summary =
        String.format(":Routing number: %s%n", this.getRoutingNumber())
        + String.format(":Account number: %s%n", this.getAccountNumber())
        + String.format(":Check number: %s%n", this.getCheckNumber())
        + String.format(":Date: %s%n", this.getDate())
        + String.format(":Amount: %s%n", this.getAmount())
        + String.format(":Payees: %s%n",
          this.getPayees().stream()
            .map(StringField::toString)
            .collect(Collectors.joining(", ")));

    return SummaryHelper.cleanSummary(summary);
  }
}
