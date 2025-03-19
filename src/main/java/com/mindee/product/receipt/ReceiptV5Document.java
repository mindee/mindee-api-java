package com.mindee.product.receipt;

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
import com.mindee.parsing.standard.StringField;
import com.mindee.parsing.standard.Taxes;
import com.mindee.parsing.standard.TaxesDeserializer;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Receipt API version 5.3 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptV5Document extends Prediction {

  /**
   * The purchase category of the receipt.
   */
  @JsonProperty("category")
  protected ClassificationField category;
  /**
   * The date the purchase was made.
   */
  @JsonProperty("date")
  protected DateField date;
  /**
   * The type of receipt: EXPENSE RECEIPT or CREDIT CARD RECEIPT.
   */
  @JsonProperty("document_type")
  protected ClassificationField documentType;
  /**
   * List of all line items on the receipt.
   */
  @JsonProperty("line_items")
  protected List<ReceiptV5LineItem> lineItems = new ArrayList<>();
  /**
   * The locale of the document.
   */
  @JsonProperty("locale")
  protected LocaleField locale;
  /**
   * The receipt number or identifier.
   */
  @JsonProperty("receipt_number")
  protected StringField receiptNumber;
  /**
   * The purchase subcategory of the receipt for transport and food.
   */
  @JsonProperty("subcategory")
  protected ClassificationField subcategory;
  /**
   * The address of the supplier or merchant.
   */
  @JsonProperty("supplier_address")
  protected StringField supplierAddress;
  /**
   * List of company registration numbers associated to the supplier.
   */
  @JsonProperty("supplier_company_registrations")
  protected List<CompanyRegistrationField> supplierCompanyRegistrations = new ArrayList<>();
  /**
   * The name of the supplier or merchant.
   */
  @JsonProperty("supplier_name")
  protected StringField supplierName;
  /**
   * The phone number of the supplier or merchant.
   */
  @JsonProperty("supplier_phone_number")
  protected StringField supplierPhoneNumber;
  /**
   * The list of taxes present on the receipt.
   */
  @JsonProperty("taxes")
  @JsonDeserialize(using = TaxesDeserializer.class)
  protected Taxes taxes;
  /**
   * The time the purchase was made.
   */
  @JsonProperty("time")
  protected StringField time;
  /**
   * The total amount of tip and gratuity.
   */
  @JsonProperty("tip")
  protected AmountField tip;
  /**
   * The total amount paid: includes taxes, discounts, fees, tips, and gratuity.
   */
  @JsonProperty("total_amount")
  protected AmountField totalAmount;
  /**
   * The net amount paid: does not include taxes, fees, and discounts.
   */
  @JsonProperty("total_net")
  protected AmountField totalNet;
  /**
   * The sum of all taxes.
   */
  @JsonProperty("total_tax")
  protected AmountField totalTax;

  @Override
  public boolean isEmpty() {
    return (
      this.locale == null
      && this.category == null
      && this.subcategory == null
      && this.documentType == null
      && this.date == null
      && this.time == null
      && this.totalAmount == null
      && this.totalNet == null
      && this.totalTax == null
      && this.tip == null
      && (this.taxes == null || this.taxes.isEmpty())
      && this.supplierName == null
      && (this.supplierCompanyRegistrations == null || this.supplierCompanyRegistrations.isEmpty())
      && this.supplierAddress == null
      && this.supplierPhoneNumber == null
      && this.receiptNumber == null
      && (this.lineItems == null || this.lineItems.isEmpty())
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Expense Locale: %s%n", this.getLocale())
    );
    outStr.append(
        String.format(":Purchase Category: %s%n", this.getCategory())
    );
    outStr.append(
        String.format(":Purchase Subcategory: %s%n", this.getSubcategory())
    );
    outStr.append(
        String.format(":Document Type: %s%n", this.getDocumentType())
    );
    outStr.append(
        String.format(":Purchase Date: %s%n", this.getDate())
    );
    outStr.append(
        String.format(":Purchase Time: %s%n", this.getTime())
    );
    outStr.append(
        String.format(":Total Amount: %s%n", this.getTotalAmount())
    );
    outStr.append(
        String.format(":Total Net: %s%n", this.getTotalNet())
    );
    outStr.append(
        String.format(":Total Tax: %s%n", this.getTotalTax())
    );
    outStr.append(
        String.format(":Tip and Gratuity: %s%n", this.getTip())
    );
    outStr.append(
        String.format(":Taxes: %s%n", this.getTaxes())
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
        String.format(":Receipt Number: %s%n", this.getReceiptNumber())
    );
    String lineItemsSummary = "";
    if (!this.getLineItems().isEmpty()) {
      int[] lineItemsColSizes = new int[]{38, 10, 14, 12};
      lineItemsSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(lineItemsColSizes, "-"))
          + "| Description                          "
          + "| Quantity "
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
