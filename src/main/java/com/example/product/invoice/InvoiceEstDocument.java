package com.example.product.invoice;

import com.example.product.balticinvoice.BalticInvoiceV1Document;
import com.mindee.parsing.standard.CompanyRegistrationField;
import com.mindee.parsing.standard.LocaleField;
import com.mindee.parsing.standard.PaymentDetailsField;
import com.mindee.parsing.standard.TaxField;
import java.util.ArrayList;

public class InvoiceEstDocument extends CombinedInvoiceDocument {

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
    if (this.invoiceNumber.isEmpty()) {
      this.invoiceNumber = document.getInvoiceNumber();
    }

    System.out.println("==================================");
    System.out.println(document.getInvoiceDate());
    System.out.println("==================================");

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
