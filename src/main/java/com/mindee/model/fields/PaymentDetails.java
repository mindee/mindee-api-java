package com.mindee.model.fields;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
public class PaymentDetails extends BaseField {

  private final String accountNumber;
  private final String iban;
  private final String routingNumber;
  private final String swift;

  @Builder
  public PaymentDetails(Boolean reconstructed, String rawValue, Double confidence,
      List<List<Double>> polygon, Integer page, String accountNumber, String iban,
      String routingNumber,
      String swift) {
    super(reconstructed, rawValue, confidence, polygon, page);
    this.accountNumber = accountNumber;
    this.iban = iban;
    this.routingNumber = routingNumber;
    this.swift = swift;
  }
}
