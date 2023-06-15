package com.mindee.product.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.field.AmountField;
import com.mindee.parsing.standard.field.ClassificationField;
import com.mindee.parsing.standard.field.CompanyRegistrationField;
import com.mindee.parsing.standard.field.DateField;
import com.mindee.parsing.standard.field.LocaleField;
import com.mindee.parsing.standard.field.PaymentDetailsField;
import com.mindee.parsing.standard.field.StringField;
import com.mindee.parsing.standard.field.TaxField;
import com.mindee.parsing.standard.field.TaxesDeserializer;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Invoice, API version 4.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceV4Document {

  /**
   * Locale information.
   */
  @JsonProperty("locale")
  private LocaleField localeField;
  /**
   * The type of the parsed document (see official documentation for the list).
   */
  @JsonProperty("document_type")
  private ClassificationField documentType;
  /**
   * The creation date of the invoice.
   */
  @JsonProperty("date")
  private DateField invoiceDateField;
  /**
   * The invoice number.
   */
  @JsonProperty("invoice_number")
  private StringField invoiceNumber;
  /**
   * List of Reference numbers including PO number.
   */
  @JsonProperty("reference_numbers")
  private List<StringField> referenceNumbers;
  /**
   * The due date of the invoice.
   */
  @JsonProperty("due_date")
  private DateField dueDateField;
  /**
   * The supplier name.
   */
  @JsonProperty("supplier_name")
  private StringField supplierName;
  /**
   * The supplier payment information's.
   */
  @JsonProperty("supplier_payment_details")
  private List<PaymentDetailsField> supplierPaymentDetails;
  /**
   * The supplier company regitration information.
   */
  @JsonProperty("supplier_company_registrations")
  private List<CompanyRegistrationField> supplierCompanyRegistrations;
  /**
   * The supplier address.
   */
  @JsonProperty("supplier_address")
  private StringField supplierAddress;
  /**
   * The name of the customer.
   */
  @JsonProperty("customer_name")
  private StringField customerName;
  /**
   * The company registration information for the customer.
   */
  @JsonProperty("customer_company_registrations")
  private List<CompanyRegistrationField> customerCompanyRegistrations;
  /**
   * The address of the customer.
   */
  @JsonProperty("customer_address")
  private StringField customerAddress;
  /**
   * The list of the taxes.
   */
  @JsonProperty("taxes")
  @JsonDeserialize(using = TaxesDeserializer.class)
  private List<TaxField> taxes;
  /**
   * The total amount with tax included.
   */
  @JsonProperty("total_amount")
  private AmountField totalAmount;
  /**
   * The total amount without the tax value.
   */
  @JsonProperty("total_net")
  private AmountField totalNet;
  /**
   * Line items details.
   */
  @JsonProperty("line_items")
  private List<InvoiceV4LineItem> lineItems;

  public Double getTotalTaxes() {
    return taxes.stream().mapToDouble(TaxField::getValue).sum();
  }

  @Override
  public String toString() {
    int[] columnSizes = new int[]{22, 9, 9, 10, 18, 38};
    String lineItemsSummary = String.format("%n");
    if (!this.getLineItems().isEmpty()) {
      lineItemsSummary =
        String.format("%n%s", SummaryHelper.lineSeparator(columnSizes, "-"))
        + String.format("%n  | Code                 | QTY     | Price   | Amount   | Tax (Rate)       | Description                          |")
        + String.format("%n%s%n  ", SummaryHelper.lineSeparator(columnSizes, "="));

      lineItemsSummary += this.getLineItems().stream()
        .map(InvoiceV4LineItem::toTableLine)
        .collect(Collectors.joining(
          String.format("%n%s%n  ", SummaryHelper.lineSeparator(columnSizes, "-")))
        );

      lineItemsSummary +=
        String.format("%n%s", SummaryHelper.lineSeparator(columnSizes, "-"));
    }

    String summary =
        String.format(":Locale: %s%n", this.getLocaleField())
        + String.format(":Document type: %s%n", this.getDocumentType())
        + String.format(":Invoice number: %s%n", this.getInvoiceNumber())
        + String.format(":Reference numbers: %s%n",
          this.getReferenceNumbers().stream()
            .map(StringField::toString)
            .collect(Collectors.joining(", ")))
        + String.format(":Invoice date: %s%n", this.getInvoiceDateField())
        + String.format(":Invoice due date: %s%n", this.getDueDateField())
        + String.format(":Supplier name: %s%n", this.getSupplierName())
        + String.format(":Supplier address: %s%n", this.getSupplierAddress())
        + String.format(":Supplier company registrations: %s%n",
          this.getSupplierCompanyRegistrations().stream()
            .map(CompanyRegistrationField::getValue)
            .collect(Collectors.joining("; ")))
        + String.format(":Supplier payment details: %s%n", this.getSupplierPaymentDetails().stream()
          .map(PaymentDetailsField::toString)
          .collect(Collectors.joining("%n                 ")))
        + String.format(":Customer name: %s%n", this.getCustomerName())
        + String.format(":Customer address: %s%n", this.getCustomerAddress())
        + String.format(":Customer company registrations: %s%n",
          this.getCustomerCompanyRegistrations().stream()
            .map(CompanyRegistrationField::getValue)
            .collect(Collectors.joining("; ")))
        + String.format(":Taxes: %s%n", this.taxes.toString())
        + String.format(":Total net: %s%n", this.getTotalNet())
        + String.format(":Total tax: %s%n", SummaryHelper.formatAmount(this.getTotalTaxes()))
        + String.format(":Total amount: %s%n",
          this.getTotalAmount())
        + String.format(":Line Items: %s%n", lineItemsSummary);

    return SummaryHelper.cleanSummary(summary);
  }
}
