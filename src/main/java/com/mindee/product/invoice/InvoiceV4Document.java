package com.mindee.product.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.parsing.standard.CompanyRegistrationField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.LocaleField;
import com.mindee.parsing.standard.PaymentDetailsField;
import com.mindee.parsing.standard.StringField;
import com.mindee.parsing.standard.Taxes;
import com.mindee.parsing.standard.TaxesDeserializer;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Invoice API version 4.8 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceV4Document extends Prediction {

  /**
   * The customer's address used for billing.
   */
  @JsonProperty("billing_address")
  protected StringField billingAddress;
  /**
   * The address of the customer.
   */
  @JsonProperty("customer_address")
  protected StringField customerAddress;
  /**
   * List of company registrations associated to the customer.
   */
  @JsonProperty("customer_company_registrations")
  protected List<CompanyRegistrationField> customerCompanyRegistrations = new ArrayList<>();
  /**
   * The customer account number or identifier from the supplier.
   */
  @JsonProperty("customer_id")
  protected StringField customerId;
  /**
   * The name of the customer or client.
   */
  @JsonProperty("customer_name")
  protected StringField customerName;
  /**
   * The date the purchase was made.
   */
  @JsonProperty("date")
  protected DateField date;
  /**
   * One of: 'INVOICE', 'CREDIT NOTE'.
   */
  @JsonProperty("document_type")
  protected ClassificationField documentType;
  /**
   * The date on which the payment is due.
   */
  @JsonProperty("due_date")
  protected DateField dueDate;
  /**
   * The invoice number or identifier.
   */
  @JsonProperty("invoice_number")
  protected StringField invoiceNumber;
  /**
   * List of line item details.
   */
  @JsonProperty("line_items")
  protected List<InvoiceV4LineItem> lineItems = new ArrayList<>();
  /**
   * The locale detected on the document.
   */
  @JsonProperty("locale")
  protected LocaleField locale;
  /**
   * The date on which the payment is due/ was full-filled.
   */
  @JsonProperty("payment_date")
  protected DateField paymentDate;
  /**
   * The purchase order number.
   */
  @JsonProperty("po_number")
  protected StringField poNumber;
  /**
   * List of Reference numbers, including PO number.
   */
  @JsonProperty("reference_numbers")
  protected List<StringField> referenceNumbers = new ArrayList<>();
  /**
   * Customer's delivery address.
   */
  @JsonProperty("shipping_address")
  protected StringField shippingAddress;
  /**
   * The address of the supplier or merchant.
   */
  @JsonProperty("supplier_address")
  protected StringField supplierAddress;
  /**
   * List of company registrations associated to the supplier.
   */
  @JsonProperty("supplier_company_registrations")
  protected List<CompanyRegistrationField> supplierCompanyRegistrations = new ArrayList<>();
  /**
   * The email of the supplier or merchant.
   */
  @JsonProperty("supplier_email")
  protected StringField supplierEmail;
  /**
   * The name of the supplier or merchant.
   */
  @JsonProperty("supplier_name")
  protected StringField supplierName;
  /**
   * List of payment details associated to the supplier.
   */
  @JsonProperty("supplier_payment_details")
  protected List<PaymentDetailsField> supplierPaymentDetails = new ArrayList<>();
  /**
   * The phone number of the supplier or merchant.
   */
  @JsonProperty("supplier_phone_number")
  protected StringField supplierPhoneNumber;
  /**
   * The website URL of the supplier or merchant.
   */
  @JsonProperty("supplier_website")
  protected StringField supplierWebsite;
  /**
   * List of tax line details.
   */
  @JsonProperty("taxes")
  @JsonDeserialize(using = TaxesDeserializer.class)
  protected Taxes taxes;
  /**
   * The total amount paid: includes taxes, tips, fees, and other charges.
   */
  @JsonProperty("total_amount")
  protected AmountField totalAmount;
  /**
   * The net amount paid: does not include taxes, fees, and discounts.
   */
  @JsonProperty("total_net")
  protected AmountField totalNet;
  /**
   * The total tax: includes all the taxes paid for this invoice.
   */
  @JsonProperty("total_tax")
  protected AmountField totalTax;

  @Override
  public boolean isEmpty() {
    return (
      this.locale == null
      && this.invoiceNumber == null
      && this.poNumber == null
      && (this.referenceNumbers == null || this.referenceNumbers.isEmpty())
      && this.date == null
      && this.dueDate == null
      && this.paymentDate == null
      && this.totalNet == null
      && this.totalAmount == null
      && this.totalTax == null
      && (this.taxes == null || this.taxes.isEmpty())
      && (this.supplierPaymentDetails == null || this.supplierPaymentDetails.isEmpty())
      && this.supplierName == null
      && (this.supplierCompanyRegistrations == null || this.supplierCompanyRegistrations.isEmpty())
      && this.supplierAddress == null
      && this.supplierPhoneNumber == null
      && this.supplierWebsite == null
      && this.supplierEmail == null
      && this.customerName == null
      && (this.customerCompanyRegistrations == null || this.customerCompanyRegistrations.isEmpty())
      && this.customerAddress == null
      && this.customerId == null
      && this.shippingAddress == null
      && this.billingAddress == null
      && this.documentType == null
      && (this.lineItems == null || this.lineItems.isEmpty())
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Locale: %s%n", this.getLocale())
    );
    outStr.append(
        String.format(":Invoice Number: %s%n", this.getInvoiceNumber())
    );
    outStr.append(
        String.format(":Purchase Order Number: %s%n", this.getPoNumber())
    );
    String referenceNumbers = SummaryHelper.arrayToString(
        this.getReferenceNumbers(),
        "%n                    "
    );
    outStr.append(
        String.format(":Reference Numbers: %s%n", referenceNumbers)
    );
    outStr.append(
        String.format(":Purchase Date: %s%n", this.getDate())
    );
    outStr.append(
        String.format(":Due Date: %s%n", this.getDueDate())
    );
    outStr.append(
        String.format(":Payment Date: %s%n", this.getPaymentDate())
    );
    outStr.append(
        String.format(":Total Net: %s%n", this.getTotalNet())
    );
    outStr.append(
        String.format(":Total Amount: %s%n", this.getTotalAmount())
    );
    outStr.append(
        String.format(":Total Tax: %s%n", this.getTotalTax())
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
        String.format(":Supplier Phone Number: %s%n", this.getSupplierPhoneNumber())
    );
    outStr.append(
        String.format(":Supplier Website: %s%n", this.getSupplierWebsite())
    );
    outStr.append(
        String.format(":Supplier Email: %s%n", this.getSupplierEmail())
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
        String.format(":Customer ID: %s%n", this.getCustomerId())
    );
    outStr.append(
        String.format(":Shipping Address: %s%n", this.getShippingAddress())
    );
    outStr.append(
        String.format(":Billing Address: %s%n", this.getBillingAddress())
    );
    outStr.append(
        String.format(":Document Type: %s%n", this.getDocumentType())
    );
    String lineItemsSummary = "";
    if (!this.getLineItems().isEmpty()) {
      int[] lineItemsColSizes = new int[]{38, 14, 10, 12, 14, 14, 17, 12};
      lineItemsSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(lineItemsColSizes, "-"))
          + "| Description                          "
          + "| Product code "
          + "| Quantity "
          + "| Tax Amount "
          + "| Tax Rate (%) "
          + "| Total Amount "
          + "| Unit of measure "
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
