package com.mindee.parsing.standard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Represent a payment detail.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDetailsField extends BaseField {

  /**
   * The account number.
   */
  @JsonProperty("account_number")
  private String accountNumber;
  /**
   * The full IBAN.
   */
  @JsonProperty("iban")
  private String iban;
  /**
   * The routing number.
   */
  @JsonProperty("routing_number")
  private String routingNumber;
  /**
   * The SWIFT value.
   */
  @JsonProperty("swift")
  private String swift;

  public boolean isEmpty() {
    return (
        (accountNumber == null || accountNumber.isEmpty())
        && (iban == null || iban.isEmpty())
        && (routingNumber == null || routingNumber.isEmpty())
        && (swift == null || swift.isEmpty())
      );
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    if (accountNumber != null && !accountNumber.isEmpty()) {
      stringBuilder.append(accountNumber);
      stringBuilder.append("; ");
    }
    if (iban != null && !iban.isEmpty()) {
      stringBuilder.append(iban);
      stringBuilder.append("; ");
    }
    if (routingNumber != null && !routingNumber.isEmpty()) {
      stringBuilder.append(routingNumber);
      stringBuilder.append("; ");
    }
    if (swift != null && !swift.isEmpty()) {
      stringBuilder.append(swift);
      stringBuilder.append("; ");
    }
    return stringBuilder.toString().trim();
  }
}
