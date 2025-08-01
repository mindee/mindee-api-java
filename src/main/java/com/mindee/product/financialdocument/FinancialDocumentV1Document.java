package com.mindee.product.financialdocument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.AddressField;
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
 * Financial Document API version 1.14 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialDocumentV1Document extends Prediction {

  /**
   * The customer's address used for billing.
   */
  @JsonProperty("billing_address")
  protected AddressField billingAddress;
  /**
   * The purchase category.
   */
  @JsonProperty("category")
  protected ClassificationField category;
  /**
   * The address of the customer.
   */
  @JsonProperty("customer_address")
  protected AddressField customerAddress;
  /**
   * List of company registration numbers associated to the customer.
   */
  @JsonProperty("customer_company_registrations")
  protected List<CompanyRegistrationField> customerCompanyRegistrations = new ArrayList<>();
  /**
   * The customer account number or identifier from the supplier.
   */
  @JsonProperty("customer_id")
  protected StringField customerId;
  /**
   * The name of the customer.
   */
  @JsonProperty("customer_name")
  protected StringField customerName;
  /**
   * The date the purchase was made.
   */
  @JsonProperty("date")
  protected DateField date;
  /**
   * The document number or identifier (invoice number or receipt number).
   */
  @JsonProperty("document_number")
  protected StringField documentNumber;
  /**
   * The type of the document: INVOICE or CREDIT NOTE if it is an invoice, CREDIT CARD RECEIPT or EXPENSE RECEIPT if it is a receipt.
   */
  @JsonProperty("document_type")
  protected ClassificationField documentType;
  /**
   * Document type extended.
   */
  @JsonProperty("document_type_extended")
  protected ClassificationField documentTypeExtended;
  /**
   * The date on which the payment is due.
   */
  @JsonProperty("due_date")
  protected DateField dueDate;
  /**
   * The invoice number or identifier only if document is an invoice.
   */
  @JsonProperty("invoice_number")
  protected StringField invoiceNumber;
  /**
   * List of line item present on the document.
   */
  @JsonProperty("line_items")
  protected List<FinancialDocumentV1LineItem> lineItems = new ArrayList<>();
  /**
   * The locale of the document.
   */
  @JsonProperty("locale")
  protected LocaleField locale;
  /**
   * The date on which the payment is due / fullfilled.
   */
  @JsonProperty("payment_date")
  protected DateField paymentDate;
  /**
   * The purchase order number, only if the document is an invoice.
   */
  @JsonProperty("po_number")
  protected StringField poNumber;
  /**
   * The receipt number or identifier only if document is a receipt.
   */
  @JsonProperty("receipt_number")
  protected StringField receiptNumber;
  /**
   * List of Reference numbers, including PO number, only if the document is an invoice.
   */
  @JsonProperty("reference_numbers")
  protected List<StringField> referenceNumbers = new ArrayList<>();
  /**
   * The customer's address used for shipping.
   */
  @JsonProperty("shipping_address")
  protected AddressField shippingAddress;
  /**
   * The purchase subcategory for transport, food and shooping.
   */
  @JsonProperty("subcategory")
  protected ClassificationField subcategory;
  /**
   * The address of the supplier or merchant.
   */
  @JsonProperty("supplier_address")
  protected AddressField supplierAddress;
  /**
   * List of company registration numbers associated to the supplier.
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
   * List of payment details associated to the supplier (only for invoices).
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
   * List of all taxes on the document.
   */
  @JsonProperty("taxes")
  @JsonDeserialize(using = TaxesDeserializer.class)
  protected Taxes taxes;
  /**
   * The time the purchase was made (only for receipts).
   */
  @JsonProperty("time")
  protected StringField time;
  /**
   * The total amount of tip and gratuity
   */
  @JsonProperty("tip")
  protected AmountField tip;
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
   * The sum of all taxes present on the document.
   */
  @JsonProperty("total_tax")
  protected AmountField totalTax;

  @Override
  public boolean isEmpty() {
    return (
      this.locale == null
      && this.invoiceNumber == null
      && this.poNumber == null
      && this.receiptNumber == null
      && this.documentNumber == null
      && (this.referenceNumbers == null || this.referenceNumbers.isEmpty())
      && this.date == null
      && this.dueDate == null
      && this.paymentDate == null
      && this.totalNet == null
      && this.totalAmount == null
      && (this.taxes == null || this.taxes.isEmpty())
      && (this.supplierPaymentDetails == null || this.supplierPaymentDetails.isEmpty())
      && this.supplierName == null
      && (this.supplierCompanyRegistrations == null || this.supplierCompanyRegistrations.isEmpty())
      && this.supplierAddress == null
      && this.supplierPhoneNumber == null
      && this.customerName == null
      && this.supplierWebsite == null
      && this.supplierEmail == null
      && (this.customerCompanyRegistrations == null || this.customerCompanyRegistrations.isEmpty())
      && this.customerAddress == null
      && this.customerId == null
      && this.shippingAddress == null
      && this.billingAddress == null
      && this.documentType == null
      && this.documentTypeExtended == null
      && this.subcategory == null
      && this.category == null
      && this.totalTax == null
      && this.tip == null
      && this.time == null
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
    outStr.append(
        String.format(":Receipt Number: %s%n", this.getReceiptNumber())
    );
    outStr.append(
        String.format(":Document Number: %s%n", this.getDocumentNumber())
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
        String.format(":Customer Name: %s%n", this.getCustomerName())
    );
    outStr.append(
        String.format(":Supplier Website: %s%n", this.getSupplierWebsite())
    );
    outStr.append(
        String.format(":Supplier Email: %s%n", this.getSupplierEmail())
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
    outStr.append(
        String.format(":Document Type Extended: %s%n", this.getDocumentTypeExtended())
    );
    outStr.append(
        String.format(":Purchase Subcategory: %s%n", this.getSubcategory())
    );
    outStr.append(
        String.format(":Purchase Category: %s%n", this.getCategory())
    );
    outStr.append(
        String.format(":Total Tax: %s%n", this.getTotalTax())
    );
    outStr.append(
        String.format(":Tip and Gratuity: %s%n", this.getTip())
    );
    outStr.append(
        String.format(":Purchase Time: %s%n", this.getTime())
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
