package com.mindee.parsing.financialdocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.AmountField;
import com.mindee.parsing.common.field.ClassificationField;
import com.mindee.parsing.common.field.CompanyRegistrationField;
import com.mindee.parsing.common.field.DateField;
import com.mindee.parsing.common.field.LocaleField;
import com.mindee.parsing.common.field.PaymentDetailsField;
import com.mindee.parsing.common.field.StringField;
import com.mindee.parsing.common.field.TaxField;
import com.mindee.parsing.common.field.TaxesDeserializer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Financial Document, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialV1DocumentPrediction {

  /**
   * Locale information.
   */
  @JsonProperty("locale")
  private LocaleField locale;

  /**
   * The type of the parsed document (see official documentation for the list).
   */
  @JsonProperty("document_type")
  private ClassificationField documentType;

  /**
   * The creation date.
   */
  @JsonProperty("date")
  private DateField date;

  /**
   * The invoice number.
   */
  @JsonProperty("invoice_number")
  private StringField invoiceNumber;

  /**
   * List of Reference numbers including PO number.
   */
  @JsonProperty("reference_numbers")
  private List<StringField> referenceNumbers = new ArrayList<>();

  /**
   * The due date of the invoice.
   */
  @JsonProperty("due_date")
  private DateField dueDate;

  /**
   * The supplier name.
   */
  @JsonProperty("supplier_name")
  private StringField supplierName;

  /**
   * The supplier company registration information.
   */
  @JsonProperty("supplier_company_registrations")
  private List<CompanyRegistrationField> supplierCompanyRegistrations = new ArrayList<>();

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
  private List<CompanyRegistrationField> customerCompanyRegistrations = new ArrayList<>();

  /**
   * The address of the customer.
   */
  @JsonProperty("customer_address")
  private StringField customerAddress;

  /**
   * The payment information's.
   */
  @JsonProperty("supplier_payment_details")
  private List<PaymentDetailsField> paymentDetails = new ArrayList<>();

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
   * The time of the purchase.
   */
  @JsonProperty("time")
  private StringField time;

  /**
   * The type of purchase among a certain list of it (see official documentation for the list).
   */
  @JsonProperty("category")
  private ClassificationField category;

  /**
   * The subcategory of purchase among a certain list of it (see official documentation for the list).
   */
  @JsonProperty("subcategory")
  private ClassificationField subCategory;

  /**
   * Total tax amount of the purchase.
   */
  @JsonProperty("total_tax")
  private AmountField totalTax;

  /**
   * Total amount of tip and gratuity.
   */
  @JsonProperty("tip")
  private AmountField tip;

  /**
   * Line items details.
   */
  @JsonProperty("line_items")
  private List<FinancialDocumentV1LineItem> lineItems = new ArrayList<>();

  @Override
  public String toString() {
    int[] columnSizes = new int[]{22, 9, 9, 10, 18, 38};
    String lineItemsSummary = "";
    if (!this.getLineItems().isEmpty()) {
      lineItemsSummary =
        String.format("%n%s", SummaryHelper.lineSeparator(columnSizes, "-"))
          + String.format("%n  | Code                 | QTY     | Price   | Amount   | Tax (Rate)       | Description                          |")
          + String.format("%n%s%n  ", SummaryHelper.lineSeparator(columnSizes, "="));

      lineItemsSummary += this.getLineItems().stream()
        .map(FinancialDocumentV1LineItem::toTableLine)
        .collect(Collectors.joining(
          String.format("%n%s%n  ", SummaryHelper.lineSeparator(columnSizes, "-")))
        );

      lineItemsSummary +=
        String.format("%n%s", SummaryHelper.lineSeparator(columnSizes, "-"));
    }

    String summary =
        String.format(":Document type: %s%n", this.getDocumentType())
        + String.format(":Category: %s%n", this.getCategory())
        + String.format(":Subcategory: %s%n", this.getSubCategory())
        + String.format(":Locale: %s%n", this.getLocale())
        + String.format(":Date: %s%n", this.getDate())
        + String.format(":Due date: %s%n", this.getDueDate())
        + String.format(":Time: %s%n", this.getTime())
        + String.format(":Number: %s%n", this.getInvoiceNumber())
        + String.format(":Reference numbers: %s%n",
          this.getReferenceNumbers().stream()
            .map(StringField::toString)
            .collect(Collectors.joining(", ")))
        + String.format(":Supplier name: %s%n", this.getSupplierName())
        + String.format(":Supplier address: %s%n", this.getSupplierAddress())
        + String.format(":Supplier company registrations: %s%n",
          this.getSupplierCompanyRegistrations().stream()
            .map(CompanyRegistrationField::getValue)
            .collect(Collectors.joining("; ")))
        + String.format(":Supplier payment details: %s%n", this.getPaymentDetails().stream()
          .map(PaymentDetailsField::toString)
          .collect(Collectors.joining("%n                 ")))
        + String.format(":Customer name: %s%n", this.getCustomerName())
        + String.format(":Customer address: %s%n", this.getCustomerAddress())
        + String.format(":Customer company registrations: %s%n",
          this.getCustomerCompanyRegistrations().stream()
            .map(CompanyRegistrationField::getValue)
            .collect(Collectors.joining("; ")))
        + String.format(":Tip: %s%n", this.getTip())
        + String.format(":Taxes: %s%n", this.taxes.toString())
        + String.format(":Total tax: %s%n", this.getTotalTax())
        + String.format(":Total net: %s%n", this.getTotalNet())
        + String.format(":Total amount: %s%n", this.getTotalAmount())
        + String.format(":Line Items: %s%n", lineItemsSummary);

    return SummaryHelper.cleanSummary(summary);
  }
}
