package com.example.product.invoice;

import com.example.product.balticinvoice.BalticInvoiceV1Document;
import com.mindee.parsing.standard.CompanyRegistrationField;
import com.mindee.parsing.standard.LocaleField;
import com.mindee.parsing.standard.TaxField;

public class InvoiceLutDocument extends CombinedInvoiceDocument {

  /**
   * Combine the invoice document with the baltic invoice document.
   */
  public void combineWithBaltic(BalticInvoiceV1Document document) {
    this.invoiceSerialNumber = document.getInvoiceSerialNumber();

    if (this.localeField.getCurrency().isEmpty()) {
      this.localeField = new LocaleField(
        this.localeField.getValue(),
        this.localeField.getLanguage(),
        this.localeField.getCountry(),
        document.getCurrency().getValue()
      );
    }
    if (this.totalAmount.isEmpty()) {
      this.totalAmount = document.getTotalAmount();
    }
    if (!document.getTotalBeforeTaxes().isEmpty()) {
      this.totalNet = document.getTotalBeforeTaxes();
    }
    if (this.invoiceNumber.isEmpty()) {
      this.invoiceNumber = document.getInvoiceNumber();
    }
    if (!document.getInvoiceDate().isEmpty()) {
      this.invoiceDateField = document.getInvoiceDate();
    }

    this.dueDateField = document.getDueDate();

    if (!document.getCustomerName().isEmpty()) {
      this.customerName = document.getCustomerName();
    }

    this.totalTax = document.getTotalTax();

    if (this.taxes.isEmpty()) {
      this.taxes.add(
          new TaxField(null, null, document.getTaxRate().getValue(), null)
      );
    }

    if (this.supplierCompanyRegistrations.isEmpty()) {
      if (!document.getSupplierVatCode().isEmpty()) {
        this.supplierCompanyRegistrations.add(
          new CompanyRegistrationField("VAT NUMBER", document.getSupplierVatCode().getValue())
        );
      }
      if (!document.getSupplierRegistrationNumber().isEmpty()) {
        this.supplierCompanyRegistrations.add(
          new CompanyRegistrationField("Registration", document.getSupplierRegistrationNumber().getValue())
        );
      }
    }

    if (this.customerCompanyRegistrations.isEmpty()) {
      if (!document.getCustomerVatCode().isEmpty()) {
        this.customerCompanyRegistrations.add(
          new CompanyRegistrationField("VAT NUMBER", document.getCustomerVatCode().getValue())
        );
      }
      if (!document.getCustomerRegistrationNumber().isEmpty()) {
        this.customerCompanyRegistrations.add(
          new CompanyRegistrationField("Registration", document.getCustomerRegistrationNumber().getValue())
        );
      }
    }
  }
}
