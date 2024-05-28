package com.example.product.balticinvoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Baltic Invoice API version 1.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalticInvoiceV1Document extends Prediction {

  /**
   * The currency used in the invoice.
   */
  @JsonProperty("currency")
  private StringField currency;
  /**
   * The name of the customer.
   */
  @JsonProperty("customer_name")
  private StringField customerName;
  /**
   * The registration number of the customer.
   */
  @JsonProperty("customer_registration_number")
  private StringField customerRegistrationNumber;
  /**
   * The VAT code of the customer.
   */
  @JsonProperty("customer_vat_code")
  private StringField customerVatCode;
  /**
   * The type of the document.
   */
  @JsonProperty("document_type")
  private ClassificationField documentType;
  /**
   * The date by which the payment for the invoice is due.
   */
  @JsonProperty("due_date")
  private DateField dueDate;
  /**
   * The date when the invoice was issued.
   */
  @JsonProperty("invoice_date")
  private DateField invoiceDate;
  /**
   * The unique number assigned to the invoice.
   */
  @JsonProperty("invoice_number")
  private StringField invoiceNumber;
  /**
   * The serial number of the invoice.
   */
  @JsonProperty("invoice_serial_number")
  private StringField invoiceSerialNumber;
  /**
   * The IBAN of the supplier's bank account.
   */
  @JsonProperty("supplier_iban")
  private StringField supplierIban;
  /**
   * The name of the supplier.
   */
  @JsonProperty("supplier_name")
  private StringField supplierName;
  /**
   * The registration number of the supplier.
   */
  @JsonProperty("supplier_registration_number")
  private StringField supplierRegistrationNumber;
  /**
   * The VAT code of the supplier.
   */
  @JsonProperty("supplier_vat_code")
  private StringField supplierVatCode;
  /**
   * The tax rate applied to the invoice.
   */
  @JsonProperty("tax_rate")
  private AmountField taxRate;
  /**
   * The total amount of the invoice, including taxes.
   */
  @JsonProperty("total_amount")
  private AmountField totalAmount;
  /**
   * The total amount of the invoice before taxes.
   */
  @JsonProperty("total_before_taxes")
  private AmountField totalBeforeTaxes;
  /**
   * The total amount of tax applied to the invoice.
   */
  @JsonProperty("total_tax")
  private AmountField totalTax;

  @Override
  public boolean isEmpty() {
    return (
      this.currency == null
      && this.totalAmount == null
      && this.totalBeforeTaxes == null
      && this.invoiceNumber == null
      && this.invoiceSerialNumber == null
      && this.invoiceDate == null
      && this.dueDate == null
      && this.supplierIban == null
      && this.supplierName == null
      && this.customerName == null
      && this.taxRate == null
      && this.totalTax == null
      && this.supplierVatCode == null
      && this.supplierRegistrationNumber == null
      && this.customerVatCode == null
      && this.customerRegistrationNumber == null
      && this.documentType == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Currency: %s%n", this.getCurrency())
    );
    outStr.append(
        String.format(":Total Amount: %s%n", this.getTotalAmount())
    );
    outStr.append(
        String.format(":Total Before Taxes: %s%n", this.getTotalBeforeTaxes())
    );
    outStr.append(
        String.format(":Invoice Number: %s%n", this.getInvoiceNumber())
    );
    outStr.append(
        String.format(":Invoice Serial Number: %s%n", this.getInvoiceSerialNumber())
    );
    outStr.append(
        String.format(":Invoice Date: %s%n", this.getInvoiceDate())
    );
    outStr.append(
        String.format(":Due Date: %s%n", this.getDueDate())
    );
    outStr.append(
        String.format(":Supplier IBAN: %s%n", this.getSupplierIban())
    );
    outStr.append(
        String.format(":Supplier Name: %s%n", this.getSupplierName())
    );
    outStr.append(
        String.format(":Customer Name: %s%n", this.getCustomerName())
    );
    outStr.append(
        String.format(":Tax Rate: %s%n", this.getTaxRate())
    );
    outStr.append(
        String.format(":Total Tax: %s%n", this.getTotalTax())
    );
    outStr.append(
        String.format(":Supplier VAT Code: %s%n", this.getSupplierVatCode())
    );
    outStr.append(
        String.format(":Supplier Registration Number: %s%n", this.getSupplierRegistrationNumber())
    );
    outStr.append(
        String.format(":Customer VAT Code: %s%n", this.getCustomerVatCode())
    );
    outStr.append(
        String.format(":Customer Registration Number: %s%n", this.getCustomerRegistrationNumber())
    );
    outStr.append(
        String.format(":Document Type: %s%n", this.getDocumentType())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
