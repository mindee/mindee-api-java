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
import java.util.List;
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

    @Builder
    public InvoicePage(Locale locale, Amount totalIncl, Amount totalExcl,
        Date invoiceDate, Field invoiceNumber, Date dueDate,
        List<Tax> taxes, Field supplier, Field supplierAddress,
        Field customerName, List<Field> customerCompanyRegistration,
        Field customerAddress, List<PaymentDetails> paymentDetails,
        List<Field> companyNumber, Amount totalTax, int page,
        Orientation orientation) {
      super(locale, totalIncl, totalExcl, invoiceDate, invoiceNumber, dueDate, taxes, supplier,
          supplierAddress, customerName, customerCompanyRegistration, customerAddress,
          paymentDetails,
          companyNumber, totalTax);
      this.page = page;
      this.orientation = orientation;
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
