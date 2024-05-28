package com.example.product.invoice;

import com.example.product.balticinvoice.BalticInvoiceV1Document;
import com.mindee.parsing.standard.CompanyRegistrationField;
import com.mindee.parsing.standard.PaymentDetailsField;
import com.mindee.parsing.standard.TaxField;
import com.mindee.product.invoice.InvoiceV4Document;
import java.util.ArrayList;

public class InvoiceLvaDocument extends InvoiceV4Document {

  /**
   * Combine the invoice document with the baltic invoice document.
   */
  public void combineWithBaltic(BalticInvoiceV1Document document) {
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

    this.supplierCompanyRegistrations = new ArrayList<>();
    if (!document.getSupplierVatCode().isEmpty()) {
      this.supplierCompanyRegistrations.add(
          new CompanyRegistrationField("VAT", document.getSupplierVatCode().getValue())
      );
    }
    if (!document.getSupplierRegistrationNumber().isEmpty()) {
      this.supplierCompanyRegistrations.add(
        new CompanyRegistrationField("Registration", document.getSupplierRegistrationNumber().getValue())
      );
    }

    this.customerCompanyRegistrations = new ArrayList<>();
    if (!document.getCustomerVatCode().isEmpty()) {
      this.customerCompanyRegistrations.add(
        new CompanyRegistrationField("VAT", document.getCustomerVatCode().getValue())
      );
    }
    if (!document.getCustomerRegistrationNumber().isEmpty()) {
      this.customerCompanyRegistrations.add(
        new CompanyRegistrationField("Registration", document.getCustomerRegistrationNumber().getValue())
      );
    }
  }
}
