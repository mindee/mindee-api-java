package com.mindee.model.fields;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.geometry.Polygon;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDetails extends BaseField {

  private final String accountNumber;
  private final String iban;
  private final String routingNumber;
  private final String swift;

  @Builder @Jacksonized
  public PaymentDetails(Boolean reconstructed, Double confidence,
    Polygon polygon, @JsonProperty("page_id") Integer page, @JsonProperty("account_number")String accountNumber, String iban,
    @JsonProperty("routing_number")String routingNumber,
      String swift) {
    super(reconstructed, confidence, polygon, page);
    this.accountNumber = accountNumber;
    this.iban = iban;
    this.routingNumber = routingNumber;
    this.swift = swift;
  }

  public String paymentDetailsSummary() {
    StringBuilder stringBuilder = new StringBuilder("");
    if (accountNumber != null && accountNumber.length() > 0) {
      stringBuilder.append(accountNumber);
      stringBuilder.append("; ");
    }
    if (iban != null && iban.length() > 0) {
      stringBuilder.append(iban);
      stringBuilder.append("; ");
    }
    if (routingNumber != null && routingNumber.length() > 0) {
      stringBuilder.append(routingNumber);
      stringBuilder.append("; ");
    }
    if (swift != null && swift.length() > 0) {
      stringBuilder.append(swift);
      stringBuilder.append("; ");
    }
    return stringBuilder.toString().trim();
  }
}
