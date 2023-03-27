package com.mindee.parsing.proofofaddress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.CompanyRegistrationField;
import com.mindee.parsing.common.field.DateField;
import com.mindee.parsing.common.field.LocaleField;
import com.mindee.parsing.common.field.StringField;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
* Document data for Proof of Address, API version 1.
*/
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProofOfAddressV1DocumentPrediction {

  /**
  * The locale detected on the document.
  */
  @JsonProperty("locale")
  private LocaleField locale;

  /**
  * The name of the person or company issuing the document.
  */
  @JsonProperty("issuer_name")
  private StringField issuerName;

  /**
  * List of company registrations found for the issuer.
  */
  @JsonProperty("issuer_company_registration")
  private List<CompanyRegistrationField> issuerCompanyRegistration;

  /**
  * The address of the document's issuer.
  */
  @JsonProperty("issuer_address")
  private StringField issuerAddress;

  /**
  * The name of the person or company receiving the document.
  */
  @JsonProperty("recipient_name")
  private StringField recipientName;

  /**
  * List of company registrations found for the recipient.
  */
  @JsonProperty("recipient_company_registration")
  private List<CompanyRegistrationField> recipientCompanyRegistration;

  /**
  * The address of the recipient.
  */
  @JsonProperty("recipient_address")
  private StringField recipientAddress;

  /**
  * List of dates found on the document.
  */
  @JsonProperty("dates")
  private List<DateField> dates;

  /**
  * The date the document was issued.
  */
  @JsonProperty("date")
  private DateField date;

  @Override
  public String toString() {
    String summary =
        String.format(":Locale: %s%n", this.getLocale())
        + String.format(":Issuer Name: %s%n", this.getIssuerName())
        + String.format(
          ":Issuer Company Registrations: %s%n",
          SummaryHelper.arrayToString(
            this.getIssuerCompanyRegistration(),
            "%n                              "
          )
        )
        + String.format(":Issuer Address: %s%n", this.getIssuerAddress())
        + String.format(":Recipient Name: %s%n", this.getRecipientName())
        + String.format(
          ":Recipient Company Registrations: %s%n",
          SummaryHelper.arrayToString(
            this.getRecipientCompanyRegistration(),
            "%n                                 "
          )
        )
        + String.format(":Recipient Address: %s%n", this.getRecipientAddress())
        + String.format(
          ":Dates: %s%n",
          SummaryHelper.arrayToString(
            this.getDates(),
            "%n        "
          )
        )
        + String.format(":Date of Issue: %s%n", this.getDate());
    return SummaryHelper.cleanSummary(summary);
  }
}
