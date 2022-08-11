package com.mindee.model.documenttype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.model.deserialization.InvoiceDeserializer;
import com.mindee.model.fields.Amount;
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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = InvoiceDeserializer.class)
@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceResponse extends BaseDocumentResponse {

  private InvoiceDocument invoice;
  private List<InvoicePage> invoices;

  @Override
  public String documentSummary() {
    StringBuilder stringBuilder = new StringBuilder("-----Invoice data-----\n");
    stringBuilder.append(String.format("Filename: %s\n", this.getFilename()));
    stringBuilder.append(
        String.format("Invoice number: %s\n", this.invoice.getInvoiceNumber().getValue()));
    stringBuilder.append(String.format("Total amount including taxes: %f\n",
        this.invoice.getTotalIncl().getValue()));
    stringBuilder.append(String.format("Total amount excluding taxes: %f\n",
        this.invoice.getTotalExcl().getValue()));
    stringBuilder.append(
        String.format("Invoice date: %s\n", this.invoice.getInvoiceDate().getValue()));
    stringBuilder.append(
        String.format("Invoice due date: %s\n", this.invoice.getDueDate().getValue()));
    stringBuilder.append(
        String.format("Supplier name: %s\n", this.invoice.getSupplier().getValue()));
    stringBuilder.append(
        String.format("Supplier address: %s\n", this.invoice.getSupplierAddress().getValue()));
    stringBuilder.append(
        String.format("Customer name: %s\n", this.invoice.getCustomerName().getValue()));
    stringBuilder.append(String.format("Customer company registration: %s\n",
        this.invoice.getCustomerCompanyRegistration().stream()
            .map(Field::getValue)
            .collect(Collectors.joining("; "))
    ));
    stringBuilder.append(
        String.format("Customer address: %s\n", this.invoice.getCustomerAddress().getValue()));
    stringBuilder.append(
        String.format("Payment details: %s\n", this.invoice.getPaymentDetails().stream()
            .map(PaymentDetails::getPaymentDetailsSummary)
            .collect(Collectors.joining("\n                 "))));
    stringBuilder.append(String.format("Company numbers: %s\n",
        this.invoice.getCompanyNumber().stream()
            .map(Field::getValue)
            .collect(Collectors.joining("; "))
    ));
    stringBuilder.append(String.format("Taxes: %s\n",
        this.invoice.getTaxes().stream()
            .map(Tax::getTaxSummary)
            .collect(Collectors.joining("\n       "))
    ));
    stringBuilder.append(
        String.format("Locale: %s\n", this.invoice.getLocale().getLocaleSummary()));
    stringBuilder.append("----------------------");
    return stringBuilder.toString();
  }

  @Getter
  @AllArgsConstructor
  @EqualsAndHashCode
  @ToString
  private static abstract class BaseInvoice {

    private final Locale locale;
    private final Amount totalIncl;
    private final Amount totalExcl;
    private final Date invoiceDate;
    private final Field invoiceNumber;
    private final Date dueDate;
    private final List<Tax> taxes;
    private final Field supplier;
    private final Field supplierAddress;
    private final Field customerName;
    private final List<Field> customerCompanyRegistration;
    private final Field customerAddress;
    private final List<PaymentDetails> paymentDetails;
    private final List<Field> companyNumber;
    private final Amount totalTax;

  }


  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  public static final class InvoicePage extends BaseInvoice {

    private final int page;
    private final Orientation orientation;
    private final PageContent fullText;

    @Builder
    public InvoicePage(Locale locale, Amount totalIncl, Amount totalExcl,
        Date invoiceDate, Field invoiceNumber, Date dueDate,
        List<Tax> taxes, Field supplier, Field supplierAddress,
        Field customerName, List<Field> customerCompanyRegistration,
        Field customerAddress, List<PaymentDetails> paymentDetails,
        List<Field> companyNumber, Amount totalTax, int page,
        Orientation orientation, PageContent fullText) {
      super(locale, totalIncl, totalExcl, invoiceDate, invoiceNumber, dueDate, taxes, supplier,
          supplierAddress, customerName, customerCompanyRegistration, customerAddress,
          paymentDetails,
          companyNumber, totalTax);
      this.page = page;
      this.orientation = orientation;
      this.fullText = fullText;
    }
  }


  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  public static final class InvoiceDocument extends BaseInvoice {

    @Builder
    public InvoiceDocument(Locale locale, Amount totalIncl, Amount totalExcl,
        Date invoiceDate, Field invoiceNumber, Date dueDate,
        List<Tax> taxes, Field supplier, Field supplierAddress,
        Field customerName, List<Field> customerCompanyRegistration,
        Field customerAddress, List<PaymentDetails> paymentDetails,
        List<Field> companyNumber, Amount totalTax) {
      super(locale, totalIncl, totalExcl, invoiceDate, invoiceNumber, dueDate, taxes, supplier,
          supplierAddress, customerName, customerCompanyRegistration, customerAddress,
          paymentDetails,
          companyNumber, totalTax);
    }
  }

}
