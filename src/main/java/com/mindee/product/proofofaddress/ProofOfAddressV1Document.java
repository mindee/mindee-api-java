package com.mindee.product.proofofaddress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.CompanyRegistrationField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.LocaleField;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Proof of Address, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProofOfAddressV1Document {

  /**
   * The date the document was issued.
   */
  @JsonProperty("date")
  private DateField date;
  /**
   * List of dates found on the document.
   */
  @JsonProperty("dates")
  private List<DateField> dates = new ArrayList<>();
  /**
   * The address of the document's issuer.
   */
  @JsonProperty("issuer_address")
  private StringField issuerAddress;
  /**
   * List of company registrations found for the issuer.
   */
  @JsonProperty("issuer_company_registration")
  private List<CompanyRegistrationField> issuerCompanyRegistration = new ArrayList<>();
  /**
   * The name of the person or company issuing the document.
   */
  @JsonProperty("issuer_name")
  private StringField issuerName;
  /**
   * The locale detected on the document.
   */
  @JsonProperty("locale")
  private LocaleField locale;
  /**
   * The address of the recipient.
   */
  @JsonProperty("recipient_address")
  private StringField recipientAddress;
  /**
   * List of company registrations found for the recipient.
   */
  @JsonProperty("recipient_company_registration")
  private List<CompanyRegistrationField> recipientCompanyRegistration = new ArrayList<>();
  /**
   * The name of the person or company receiving the document.
   */
  @JsonProperty("recipient_name")
  private StringField recipientName;

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Locale: %s%n", this.getLocale())
    );
    outStr.append(
        String.format(":Issuer Name: %s%n", this.getIssuerName())
    );
    String issuerCompanyRegistration = SummaryHelper.arrayToString(
        this.getIssuerCompanyRegistration(),
        "%n                               "
    );
    outStr.append(
        String.format(":Issuer Company Registrations: %s%n", issuerCompanyRegistration)
    );
    outStr.append(
        String.format(":Issuer Address: %s%n", this.getIssuerAddress())
    );
    outStr.append(
        String.format(":Recipient Name: %s%n", this.getRecipientName())
    );
    String recipientCompanyRegistration = SummaryHelper.arrayToString(
        this.getRecipientCompanyRegistration(),
        "%n                                  "
    );
    outStr.append(
        String.format(":Recipient Company Registrations: %s%n", recipientCompanyRegistration)
    );
    outStr.append(
        String.format(":Recipient Address: %s%n", this.getRecipientAddress())
    );
    String dates = SummaryHelper.arrayToString(
        this.getDates(),
        "%n        "
    );
    outStr.append(
        String.format(":Dates: %s%n", dates)
    );
    outStr.append(
        String.format(":Date of Issue: %s%n", this.getDate())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
