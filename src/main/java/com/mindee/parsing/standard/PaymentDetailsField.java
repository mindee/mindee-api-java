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
  private final String accountNumber;

  /**
   * The full IBAN.
   */
  private final String iban;

  /**
   * The routing number.
   */
  private final String routingNumber;

  /**
   * The SWIFT value.
   */
  private final String swift;

  public PaymentDetailsField(
      @JsonProperty("account_number")
      String accountNumber,
      @JsonProperty("iban")
      String iban,
      @JsonProperty("routing_number")
      String routingNumber,
      @JsonProperty("swift")
      String swift
  ) {
    this.accountNumber = accountNumber;
    this.iban = iban;
    this.routingNumber = routingNumber;
    this.swift = swift;
  }

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
