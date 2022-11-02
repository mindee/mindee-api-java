package com.mindee.model.documenttype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.documenttype.InvoiceV3Response.InvoiceDocument;
import com.mindee.model.documenttype.InvoiceV3Response.InvoicePage;
import com.mindee.model.fields.Amount;
import com.mindee.model.fields.CompanyRegistration;
import com.mindee.model.fields.Date;
import com.mindee.model.fields.Field;
import com.mindee.model.fields.Locale;
import com.mindee.model.fields.Orientation;
import com.mindee.model.fields.PaymentDetails;
import com.mindee.model.fields.Tax;
import com.mindee.model.ocr.PageContent;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceV3Response extends BaseDocumentResponse implements PredictionApiResponse<InvoiceDocument, InvoicePage> {

  private InvoiceDocument document;
  private List<InvoicePage> pages;

  @Override
  public String documentSummary() {
    StringBuilder stringBuilder = new StringBuilder("-----Invoice data-----\n");
    stringBuilder.append(String.format("Filename: %s\n", this.getFilename()));
    stringBuilder.append(
        String.format("Invoice number: %s\n", this.document.getInvoiceNumber().getValue()));
    stringBuilder.append(String.format("Total amount including taxes: %f\n",
        this.document.getTotalIncl().getValue()));
    stringBuilder.append(String.format("Total amount excluding taxes: %f\n",
        this.document.getTotalExcl().getValue()));
    stringBuilder.append(
        String.format("Invoice date: %s\n", this.document.getInvoiceDate().getValue()));
    stringBuilder.append(
        String.format("Invoice due date: %s\n", this.document.getDueDate().getValue()));
    stringBuilder.append(
        String.format("Supplier name: %s\n", this.document.getSupplier().getValue()));
    stringBuilder.append(
        String.format("Supplier address: %s\n", this.document.getSupplierAddress().getValue()));
    stringBuilder.append(
        String.format("Customer name: %s\n", this.document.getCustomerName().getValue()));
    stringBuilder.append(String.format("Customer company registration: %s\n",
        this.document.getCustomerCompanyRegistration().stream()
            .map(CompanyRegistration::getValue)
            .collect(Collectors.joining("; "))
    ));
    stringBuilder.append(
        String.format("Customer address: %s\n", this.document.getCustomerAddress().getValue()));
    stringBuilder.append(
        String.format("Payment details: %s\n", this.document.getPaymentDetails().stream()
            .map(PaymentDetails::paymentDetailsSummary)
            .collect(Collectors.joining("\n                 "))));
    stringBuilder.append(String.format("Company numbers: %s\n",
        this.document.getCompanyNumber().stream()
            .map(CompanyRegistration::getValue)
            .collect(Collectors.joining("; "))
    ));
    stringBuilder.append(String.format("Taxes: %s\n",
        this.document.getTaxes().stream()
            .map(Tax::taxSummary)
            .collect(Collectors.joining("\n       "))
    ));
    stringBuilder.append(
        String.format("Locale: %s\n", this.document.getLocale().localeSummary()));
    stringBuilder.append("----------------------");
    return stringBuilder.toString();
  }

  @Getter
  @AllArgsConstructor
  @EqualsAndHashCode
  @ToString
  @SuperBuilder(toBuilder = true)
  private static abstract class BaseInvoice {

    private final Locale locale;
    @JsonProperty("total_incl")
    private final Amount totalIncl;
    @JsonProperty("total_excl")
    private final Amount totalExcl;
    @JsonProperty("date")
    private final Date invoiceDate;
    @JsonProperty("invoice_number")
    private final Field invoiceNumber;
    @JsonProperty("due_date")
    private final Date dueDate;
    @JsonProperty("taxes")
    private final List<Tax> taxes;
    private final Field supplier;
    @JsonProperty("supplier_address")
    private final Field supplierAddress;
    @JsonProperty("customer")
    private final Field customerName;
    @JsonProperty("customer_company_registration")
    private final List<CompanyRegistration> customerCompanyRegistration;
    @JsonProperty("customer_address")
    private final Field customerAddress;
    @JsonProperty("payment_details")
    private final List<PaymentDetails> paymentDetails;
    @JsonProperty("company_registration")
    private final List<CompanyRegistration> companyNumber;
    private final Amount totalTax;
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @SuperBuilder(toBuilder = true)
  @Jacksonized
  public static final class InvoicePage extends BaseInvoice implements PageLevelResponse {

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
  public static final class InvoiceDocument extends BaseInvoice implements DocumentLevelResponse {
  }

}
