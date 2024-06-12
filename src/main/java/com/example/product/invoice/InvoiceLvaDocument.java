package com.example.product.invoice;

import com.example.product.balticinvoice.BalticInvoiceV1Document;
import com.mindee.parsing.standard.CompanyRegistrationField;
import com.mindee.parsing.standard.PaymentDetailsField;
import com.mindee.parsing.standard.TaxField;

/**
 * Invoice prediction data for Latvia.
 */
public class InvoiceLvaDocument extends CombinedInvoiceDocument {

  /**
   * Combine the invoice document with the baltic invoice document.
   */
  public void combineWithBaltic(BalticInvoiceV1Document document) {
    this.invoiceSerialNumber = document.getInvoiceSerialNumber();

    if (this.totalAmount.isEmpty()) {
      this.totalAmount = document.getTotalAmount();
    }
    if (this.totalNet.isEmpty()) {
      this.totalNet = document.getTotalBeforeTaxes();
    }
    if (this.invoiceNumber.isEmpty()) {
      this.invoiceNumber = document.getInvoiceNumber();
    }
    if (!document.getInvoiceDate().isEmpty()) {
      this.invoiceDateField = document.getInvoiceDate();
    }
    if (!document.getDueDate().isEmpty()) {
      this.dueDateField = document.getDueDate();
    }
    if (this.supplierPaymentDetails.isEmpty()) {
      this.supplierPaymentDetails.add(
          new PaymentDetailsField(
              null,
              document.getSupplierIban().getValue(),
              null,
              null
          )
      );
    }
    if (this.supplierName.isEmpty()) {
      this.supplierName = document.getSupplierName();
    }
    if (this.customerName.isEmpty()) {
      this.customerName = document.getCustomerName();
    }
    if (this.taxes.isEmpty()) {
      this.taxes.add(
        new TaxField(null, null, document.getTaxRate().getValue(), null)
      );
    }

    if (!document.getSupplierVatCode().isEmpty()) {
      this.supplierCompanyRegistrations.removeIf(reg -> reg.getType().equals("VAT NUMBER"));
      this.supplierCompanyRegistrations.add(
          new CompanyRegistrationField("VAT NUMBER", document.getSupplierVatCode().getValue())
      );
    }
    if (!document.getSupplierRegistrationNumber().isEmpty()) {
      this.supplierCompanyRegistrations.add(
        new CompanyRegistrationField("Registration", document.getSupplierRegistrationNumber().getValue())
      );
    }

    if (!document.getCustomerVatCode().isEmpty()) {
      this.customerCompanyRegistrations.removeIf(reg -> reg.getType().equals("VAT NUMBER"));
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
