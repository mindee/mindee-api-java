package com.mindee.product.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.parsing.standard.CompanyRegistrationField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.LocaleField;
import com.mindee.parsing.standard.PaymentDetailsField;
import com.mindee.parsing.standard.StringField;
import com.mindee.parsing.standard.TaxField;
import com.mindee.parsing.standard.TaxesDeserializer;
import java.util.List;
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
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Locale: %s%n", this.getLocaleField())
    );
    outStr.append(
        String.format(":Invoice Number: %s%n", this.getInvoiceNumber())
    );
    String referenceNumbers = SummaryHelper.arrayToString(
        this.getReferenceNumbers(),
        "%n                    "
    );
    outStr.append(
        String.format(":Reference Numbers: %s%n", referenceNumbers)
    );
    outStr.append(
        String.format(":Purchase Date: %s%n", this.getInvoiceDateField())
    );
    outStr.append(
        String.format(":Due Date: %s%n", this.getDueDateField())
    );
    outStr.append(
        String.format(":Total Net: %s%n", this.getTotalNet())
    );
    outStr.append(
        String.format(":Total Amount: %s%n", this.getTotalAmount())
    );
    outStr.append(
        String.format(":Taxes: %s%n", this.getTaxes())
    );
    String supplierPaymentDetails = SummaryHelper.arrayToString(
        this.getSupplierPaymentDetails(),
        "%n                           "
    );
    outStr.append(
        String.format(":Supplier Payment Details: %s%n", supplierPaymentDetails)
    );
    outStr.append(
        String.format(":Supplier Name: %s%n", this.getSupplierName())
    );
    String supplierCompanyRegistrations = SummaryHelper.arrayToString(
        this.getSupplierCompanyRegistrations(),
        "%n                                 "
    );
    outStr.append(
        String.format(":Supplier Company Registrations: %s%n", supplierCompanyRegistrations)
    );
    outStr.append(
        String.format(":Supplier Address: %s%n", this.getSupplierAddress())
    );
    outStr.append(
        String.format(":Customer Name: %s%n", this.getCustomerName())
    );
    String customerCompanyRegistrations = SummaryHelper.arrayToString(
        this.getCustomerCompanyRegistrations(),
        "%n                                 "
    );
    outStr.append(
        String.format(":Customer Company Registrations: %s%n", customerCompanyRegistrations)
    );
    outStr.append(
        String.format(":Customer Address: %s%n", this.getCustomerAddress())
    );
    outStr.append(
        String.format(":Document Type: %s%n", this.getDocumentType())
    );
    String lineItemsSummary = "";
    if (!this.getLineItems().isEmpty()) {
      int[] lineItemsColSizes = new int[]{38, 14, 10, 12, 14, 14, 12};
      lineItemsSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(lineItemsColSizes, "-"))
          + "| Description                          "
          + "| Product code "
          + "| Quantity "
          + "| Tax Amount "
          + "| Tax Rate (%) "
          + "| Total Amount "
          + "| Unit Price "
          + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(lineItemsColSizes, "="));
      lineItemsSummary += SummaryHelper.arrayToString(this.getLineItems(), lineItemsColSizes);
      lineItemsSummary += String.format("%n%s", SummaryHelper.lineSeparator(lineItemsColSizes, "-"));
    }
    outStr.append(
        String.format(":Line Items: %s%n", lineItemsSummary)
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
