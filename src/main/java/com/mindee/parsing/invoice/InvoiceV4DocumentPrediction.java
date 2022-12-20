package com.mindee.parsing.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceV4DocumentPrediction {

  @JsonProperty("locale")
  private LocaleField localeField;
  @JsonProperty("date")
  private DateField invoiceDateField;
  @JsonProperty("invoice_number")
  private StringField invoiceNumber;
  @JsonProperty("due_date")
  private DateField dueDateField;
  @JsonProperty("supplier_name")
  private StringField supplierName;
  @JsonProperty("supplier_payment_details")
  private List<PaymentDetailsField> supplierFieldPaymentDetails;
  @JsonProperty("supplier_company_registrations")
  private List<CompanyRegistrationField> supplierCompanyRegistrationFields;
  @JsonProperty("supplier_address")
  private StringField supplierAddress;
  @JsonProperty("customer_name")
  private StringField customerName;
  @JsonProperty("customer_company_registrations")
  private List<CompanyRegistrationField> customerCompanyRegistrationFields;
  @JsonProperty("customer_address")
  private StringField customerAddress;
  @JsonProperty("taxes")
  private List<TaxField> taxFields;
  @JsonProperty("total_amount")
  private AmountField totalAmount;
  @JsonProperty("total_net")
  private AmountField totalNet;
  @JsonProperty("line_items")
  private List<InvoiceLineItem> lineItems;

  @Override
  public String toString() {
    String lineItemsSummary = "\n";
    if (!this.getLineItems().isEmpty()) {
      lineItemsSummary =
        "\n  Code           | QTY    | Price   | Amount   | Tax (Rate)     | Description\n  ";
      lineItemsSummary += this.getLineItems().stream()
        .map(InvoiceLineItem::toString)
        .collect(Collectors.joining("\n  "));
    }

    String summary =
      String.format("Locale: %s%n", this.getLocaleField()) +
        String.format("Invoice number: %s%n", this.getInvoiceNumber()) +
        String.format("Invoice date: %s%n", this.getInvoiceDateField()) +
        String.format("Invoice due date: %s%n", this.getDueDateField()) +
        String.format("Supplier name: %s%n", this.getSupplierName()) +
        String.format("Supplier address: %s%n", this.getSupplierAddress()) +
        String.format("Supplier company registrations: %s%n",
          this.getSupplierCompanyRegistrationFields().stream()
            .map(CompanyRegistrationField::getValue)
            .collect(Collectors.joining("; "))) +
        String.format("Supplier payment details: %s%n", this.getSupplierFieldPaymentDetails().stream()
          .map(PaymentDetailsField::toString)
          .collect(Collectors.joining("%n                 "))) +
        String.format("Customer name: %s%n", this.getCustomerName()) +
        String.format("Customer company registrations: %s%n",
          this.getCustomerCompanyRegistrationFields().stream()
            .map(CompanyRegistrationField::getValue)
            .collect(Collectors.joining("; "))) +
        String.format("Customer address: %s%n", this.getCustomerAddress()) +
        String.format("Line Items: %s%n", lineItemsSummary) +
        String.format("Taxes: %s%n",
          this.getTaxFields().stream()
            .map(TaxField::toString)
            .collect(Collectors.joining("%n       "))) +
        String.format("Total taxes: %s%n",
          this.getTaxFields().stream()
            .map(TaxField::toString)
            .collect(Collectors.joining("%n       "))) +
        String.format("Total amount excluding taxes: %s%n",
          this.getTotalNet()) +
        String.format("Total amount including taxes: %s%n",
          this.getTotalAmount()) +
        String.format("----------------------%n");

    return SummaryHelper.cleanSummary(summary);
  }
}
