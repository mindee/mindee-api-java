package com.mindee.model.documenttype.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.documenttype.BaseDocumentResponse;
import com.mindee.model.documenttype.DocumentLevelResponse;
import com.mindee.model.documenttype.PageLevelResponse;
import com.mindee.model.documenttype.PredictionApiResponse;
import com.mindee.model.documenttype.invoice.InvoiceV4Response.InvoiceV4Document;
import com.mindee.model.documenttype.invoice.InvoiceV4Response.InvoiceV4Page;
import com.mindee.model.fields.*;
import com.mindee.model.ocr.PageContent;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.stream.Collectors;

@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceV4Response extends BaseDocumentResponse
  implements PredictionApiResponse<InvoiceV4Document, InvoiceV4Page> {

  private InvoiceV4Document document;
  private List<InvoiceV4Page> pages;

  @Override
  public String documentSummary() {
    String lineItems = "\n";
    if (!this.document.getLineItems().isEmpty()) {
      lineItems =
        "\n  Code           | QTY    | Price   | Amount   | Tax (Rate)     | Description\n  ";
      lineItems += this.document.getLineItems().stream()
        .map(InvoiceLineItem::toString)
        .collect(Collectors.joining("\n  "));
    }

    return "----- Invoice V4 -----\n" + String.format("Filename: %s%n", this.getFilename()) +
      String.format("Invoice number: %s%n", this.document.getInvoiceNumber().getValue()) +
      String.format("Invoice date: %s%n", this.document.getInvoiceDate().getValue()) +
      String.format("Invoice due date: %s%n", this.document.getDueDate().getValue()) +
      String.format("Supplier name: %s%n", this.document.getSupplierName().getValue()) +
      String.format("Supplier address: %s%n", this.document.getSupplierAddress().getValue()) +
      String.format("Supplier company registrations: %s%n",
        this.document.getSupplierCompanyRegistrations().stream()
          .map(CompanyRegistration::getValue)
          .collect(Collectors.joining("; "))) +
      String.format("Supplier payment details: %s%n", this.document.getSupplierPaymentDetails().stream()
        .map(PaymentDetails::paymentDetailsSummary)
        .collect(Collectors.joining("%n                 "))) +
      String.format("Customer name: %s%n", this.document.getCustomerName().getValue()) +
      String.format("Customer company registrations: %s%n",
        this.document.getCustomerCompanyRegistrations().stream()
          .map(CompanyRegistration::getValue)
          .collect(Collectors.joining("; "))) +
      String.format("Customer address: %s%n", this.document.getCustomerAddress().getValue()) +
      String.format("Line items: %s%n", lineItems) +
      String.format("Taxes: %s%n",
        this.document.getTaxes().stream()
          .map(Tax::taxSummary)
          .collect(Collectors.joining("%n       "))) +
      String.format("Total taxes: %s%n",
        this.document.getTaxes().stream()
          .map(Tax::taxSummary)
          .collect(Collectors.joining("%n       "))) +
      String.format("Total amount excluding taxes: %f%n",
        this.document.getTotalNet().getValue()) +
      String.format("Total amount including taxes: %f%n",
        this.document.getTotalAmount().getValue()) +
      String.format("Locale: %s%n", this.document.getLocale().localeSummary()) +
      "----------------------";
  }

  @Getter
  @AllArgsConstructor
  @EqualsAndHashCode
  @ToString
  @SuperBuilder(toBuilder = true)
  private abstract static class BaseInvoiceV4 {
    @JsonProperty("locale")
    private final Locale locale;
    @JsonProperty("date")
    private final Date invoiceDate;
    @JsonProperty("invoice_number")
    private final Field invoiceNumber;
    @JsonProperty("due_date")
    private final Date dueDate;
    @JsonProperty("supplier_name")
    private final Field supplierName;
    @JsonProperty("supplier_payment_details")
    private final List<PaymentDetails> supplierPaymentDetails;
    @JsonProperty("supplier_company_registrations")
    private final List<CompanyRegistration> supplierCompanyRegistrations;
    @JsonProperty("supplier_address")
    private final Field supplierAddress;
    @JsonProperty("customer_name")
    private final Field customerName;
    @JsonProperty("customer_company_registrations")
    private final List<CompanyRegistration> customerCompanyRegistrations;
    @JsonProperty("customer_address")
    private final Field customerAddress;
    @JsonProperty("taxes")
    private final List<Tax> taxes;
    @JsonProperty("total_amount")
    private final Amount totalAmount;
    @JsonProperty("total_net")
    private final Amount totalNet;
    @JsonProperty("line_items")
    private final List<InvoiceLineItem> lineItems;
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @SuperBuilder(toBuilder = true)
  @Jacksonized
  public static final class InvoiceV4Page extends BaseInvoiceV4 implements PageLevelResponse {

    private final int page;
    private final Orientation orientation;
    @JsonProperty("page_content")
    private final PageContent fullText;

  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @SuperBuilder(toBuilder = true)
  @Jacksonized
  public static final class InvoiceV4Document extends BaseInvoiceV4 implements DocumentLevelResponse {
  }
}
