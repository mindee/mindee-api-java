package com.mindee.parsing.common.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDetailsField extends BaseField {

  @JsonProperty("account_number")
  private String accountNumber;
  @JsonProperty("iban")
  private String iban;
  @JsonProperty("routing_number")
  private String routingNumber;
  @JsonProperty("swift")
  private String swift;

  @Override
  public String toString() {
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
