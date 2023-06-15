package com.mindee.product.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.field.AmountField;
import com.mindee.parsing.standard.field.ClassificationField;
import com.mindee.parsing.standard.field.CompanyRegistrationField;
import com.mindee.parsing.standard.field.DateField;
import com.mindee.parsing.standard.field.LocaleField;
import com.mindee.parsing.standard.field.StringField;
import com.mindee.parsing.standard.field.Taxes;
import com.mindee.parsing.standard.field.TaxesDeserializer;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Expense Receipt, API version 5.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptV5DocumentPrediction {

  /**
   * The purchase category among predefined classes.
   */
  @JsonProperty("category")
  private ClassificationField category;
  /**
   * The date the purchase was made.
   */
  @JsonProperty("date")
  private DateField date;
  /**
   * One of: 'CREDIT CARD RECEIPT', 'EXPENSE RECEIPT'.
   */
  @JsonProperty("document_type")
  private ClassificationField documentType;
  /**
   * List of line item details.
   */
  @JsonProperty("line_items")
  private List<ReceiptV5LineItem> lineItems = new ArrayList<>();
  /**
   * The locale detected on the document.
   */
  @JsonProperty("locale")
  private LocaleField locale;
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
   * The total amount of tip and gratuity.
   */
  @JsonProperty("tip")
  private AmountField tip;
  /**
   * The total amount paid: includes taxes, discounts, fees, tips, and gratuity.
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
