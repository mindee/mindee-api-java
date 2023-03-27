package com.mindee.parsing.proofofaddress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.CompanyRegistrationField;
import com.mindee.parsing.common.field.DateField;
import com.mindee.parsing.common.field.LocaleField;
import com.mindee.parsing.common.field.StringField;
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
public class ProofOfAddressV1DocumentPrediction {
  /**
   * Locale information.
   */
  @JsonProperty("locale")
  private LocaleField locale;
  /**
   * ISO date yyyy-mm-dd. Works both for European and US dates.
   */
  @JsonProperty("date")
  private DateField issuanceDate;
  /**
   * All extracted ISO date yyyy-mm-dd. Works both for European and US dates.
   */
  @JsonProperty("dates")
  private List<DateField> dates = new ArrayList<>();
  /**
   * Name of the document's issuer.
   */
  @JsonProperty("issuer_name")
  private StringField issuerName;
  /**
   * Address of the document's issuer.
   */
  @JsonProperty("issuer_address")
  private StringField issuerAddress;
  /**
   * Generic: VAT NUMBER, TAX ID, COMPANY REGISTRATION NUMBER or country specific.
   */
  @JsonProperty("issuer_company_registration")
  private List<CompanyRegistrationField> issuerCompanyRegistration = new ArrayList<>();
  /**
   * Name of the document's recipient.
   */
  @JsonProperty("recipient_name")
  private StringField recipientName;
  /**
   * The surname of the person.
   */
  @JsonProperty("recipient_address")
  private StringField recipientAddress;
  /**
   * Generic: VAT NUMBER, TAX ID, COMPANY REGISTRATION NUMBER or country specific.
   */
  @JsonProperty("recipient_company_registration")
  private List<CompanyRegistrationField> recipientCompanyRegistration = new ArrayList<>();

  @Override
  public String toString() {
    String summary =
        String.format(":Locale: %s%n", this.getLocale())
        + String.format(":Issuer name: %s%n", this.getIssuerName())
        + String.format(":Issuer Address: %s%n", this.getIssuerAddress())
        + String.format(
          ":Issuer Company Registrations: %s%n",
          SummaryHelper.arrayToString(this.getIssuerCompanyRegistration(), " ")
        )
        + String.format(":Recipient name: %s%n", this.getRecipientName())
        + String.format(":Recipient Address: %s%n", this.getRecipientAddress())
        + String.format(
          ":Recipient Company Registrations: %s%n",
          SummaryHelper.arrayToString(this.getRecipientCompanyRegistration(), " ")
        )
        + String.format(":Issuance date: %s%n", this.getIssuanceDate())
        + String.format(
          ":Dates: %s%n",
          SummaryHelper.arrayToString(this.getDates(), "%n        ")
        );
    return SummaryHelper.cleanSummary(summary);
  }
}
