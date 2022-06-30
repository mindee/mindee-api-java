package com.mindee.model.documenttype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.model.deserialization.FinancialDocumentDeserializer;
import com.mindee.model.fields.Amount;
import com.mindee.model.fields.Date;
import com.mindee.model.fields.Field;
import com.mindee.model.fields.Locale;
import com.mindee.model.fields.Orientation;
import com.mindee.model.fields.PaymentDetails;
import com.mindee.model.fields.Tax;
import com.mindee.model.fields.Time;
import com.mindee.model.ocr.PageContent;
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
@JsonDeserialize(using = FinancialDocumentDeserializer.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
@Data
public class FinancialDocumentResponse extends BaseDocumentResponse {

  private FinancialDocument financialDocument;
  private List<FinancialDocumentPage> financialDocuments;

  @Getter
  @AllArgsConstructor
  @EqualsAndHashCode
  @ToString
  private static abstract class BaseFinancialDocument {

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
    private final Date date;
    private final Field category;
    private final Field merchantName;
    private final Time time;


  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  public static final class FinancialDocumentPage extends BaseFinancialDocument {

    private final int page;
    private final Orientation orientation;
    private final PageContent fullText;


    @Builder
    public FinancialDocumentPage(Locale locale, Amount totalIncl,
        Amount totalExcl, Date invoiceDate, Field invoiceNumber, Date dueDate,
        List<Tax> taxes, Field supplier, Field supplierAddress,
        Field customerName, List<Field> customerCompanyRegistration,
        Field customerAddress, List<PaymentDetails> paymentDetails,
        List<Field> companyNumber, Amount totalTax, Date date, Field category,
        Field merchantName, Time time, int page, Orientation orientation, PageContent fullText) {
      super(locale, totalIncl, totalExcl, invoiceDate, invoiceNumber, dueDate, taxes, supplier,
          supplierAddress, customerName, customerCompanyRegistration, customerAddress,
          paymentDetails,
          companyNumber, totalTax, date, category, merchantName, time);
      this.page = page;
      this.orientation = orientation;
      this.fullText = fullText;
    }
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  public static final class FinancialDocument extends BaseFinancialDocument {


    @Builder
    public FinancialDocument(Locale locale, Amount totalIncl,
        Amount totalExcl, Date invoiceDate, Field invoiceNumber, Date dueDate,
        List<Tax> taxes, Field supplier, Field supplierAddress,
        Field customerName, List<Field> customerCompanyRegistration,
        Field customerAddress, List<PaymentDetails> paymentDetails,
        List<Field> companyNumber, Amount totalTax, Date date, Field category,
        Field merchantName, Time time) {
      super(locale, totalIncl, totalExcl, invoiceDate, invoiceNumber, dueDate, taxes, supplier,
          supplierAddress, customerName, customerCompanyRegistration, customerAddress,
          paymentDetails,
          companyNumber, totalTax, date, category, merchantName, time);

    }
  }


}
