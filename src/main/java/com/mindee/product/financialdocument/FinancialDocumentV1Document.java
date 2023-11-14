package com.mindee.product.financialdocument;

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
import com.mindee.parsing.standard.Taxes;
import com.mindee.parsing.standard.TaxesDeserializer;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Financial Document, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialDocumentV1Document {

  /**
   * The purchase category among predefined classes.
   */
  @JsonProperty("category")
  private ClassificationField category;
  /**
   * The address of the customer.
   */
  @JsonProperty("customer_address")
  private StringField customerAddress;
  /**
   * List of company registrations associated to the customer.
   */
  @JsonProperty("customer_company_registrations")
  private List<CompanyRegistrationField> customerCompanyRegistrations = new ArrayList<>();
  /**
   * The name of the customer.
   */
  @JsonProperty("customer_name")
  private StringField customerName;
  /**
   * The date the purchase was made.
   */
  @JsonProperty("date")
  private DateField date;
  /**
   * One of: 'INVOICE', 'CREDIT NOTE', 'CREDIT CARD RECEIPT', 'EXPENSE RECEIPT'.
   */
  @JsonProperty("document_type")
  private ClassificationField documentType;
  /**
   * The date on which the payment is due.
   */
  @JsonProperty("due_date")
  private DateField dueDate;
  /**
   * The invoice number or identifier.
   */
  @JsonProperty("invoice_number")
  private StringField invoiceNumber;
  /**
   * List of line item details.
   */
  @JsonProperty("line_items")
  private List<FinancialDocumentV1LineItem> lineItems = new ArrayList<>();
  /**
   * The locale detected on the document.
   */
  @JsonProperty("locale")
  private LocaleField locale;
  /**
   * List of Reference numbers, including PO number.
   */
  @JsonProperty("reference_numbers")
  private List<StringField> referenceNumbers = new ArrayList<>();
  /**
   * The purchase subcategory among predefined classes for transport and food.
   */
  @JsonProperty("subcategory")
  private ClassificationField subcategory;
  /**
   * The address of the supplier or merchant.
   */
  @JsonProperty("supplier_address")
  private StringField supplierAddress;
  /**
   * List of company registrations associated to the supplier.
   */
  @JsonProperty("supplier_company_registrations")
  private List<CompanyRegistrationField> supplierCompanyRegistrations = new ArrayList<>();
  /**
   * The name of the supplier or merchant.
   */
  @JsonProperty("supplier_name")
  private StringField supplierName;
  /**
   * List of payment details associated to the supplier.
   */
  @JsonProperty("supplier_payment_details")
  private List<PaymentDetailsField> supplierPaymentDetails = new ArrayList<>();
  /**
   * The phone number of the supplier or merchant.
   */
  @JsonProperty("supplier_phone_number")
  private StringField supplierPhoneNumber;
  /**
   * List of tax lines information.
   */
  @JsonProperty("taxes")
  @JsonDeserialize(using = TaxesDeserializer.class)
  private Taxes taxes;
  /**
   * The time the purchase was made.
   */
  @JsonProperty("time")
  private StringField time;
  /**
   * The total amount of tip and gratuity
   */
  @JsonProperty("tip")
  private AmountField tip;
  /**
   * The total amount paid: includes taxes, tips, fees, and other charges.
   */
  @JsonProperty("total_amount")
  private AmountField totalAmount;
  /**
   * The net amount paid: does not include taxes, fees, and discounts.
   */
  @JsonProperty("total_net")
  private AmountField totalNet;
  /**
   * The total amount of taxes.
   */
  @JsonProperty("total_tax")
  private AmountField totalTax;

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Locale: %s%n", this.getLocale())
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
        String.format(":Purchase Date: %s%n", this.getDate())
    );
    outStr.append(
        String.format(":Due Date: %s%n", this.getDueDate())
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
