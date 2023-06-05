package com.mindee.parsing.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.AmountField;
import com.mindee.parsing.common.field.ClassificationField;
import com.mindee.parsing.common.field.CompanyRegistrationField;
import com.mindee.parsing.common.field.DateField;
import com.mindee.parsing.common.field.LocaleField;
import com.mindee.parsing.common.field.StringField;
import com.mindee.parsing.common.field.Taxes;
import com.mindee.parsing.common.field.TaxesDeserializer;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Receipt, API version 5.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptV5DocumentPrediction {

  /**
   * The receipt category among predefined classes.
   */
  @JsonProperty("category")
  private ClassificationField category;
  /**
   * The date the purchase was made.
   */
  @JsonProperty("date")
  private DateField date;
  /**
   * The receipt document type provides the information whether the document is an expense receipt or a credit card receipt.
   */
  @JsonProperty("document_type")
  private ClassificationField documentType;
  /**
   * Full extraction of lines, including: description, quantity, unit price and total.
   */
  @JsonProperty("line_items")
  private List<ReceiptV5LineItem> lineItems = new ArrayList<>();
  /**
   * The locale identifier in BCP 47 (RFC 5646) format: ISO language code, '-', ISO country code.
   */
  @JsonProperty("locale")
  private LocaleField locale;
  /**
   * The receipt sub category among predefined classes for transport and food.
   */
  @JsonProperty("subcategory")
  private ClassificationField subcategory;
  /**
   * The address of the supplier or merchant returned as a single string.
   */
  @JsonProperty("supplier_address")
  private StringField supplierAddress;
  /**
   * List of supplier company registrations or identifiers.
   */
  @JsonProperty("supplier_company_registrations")
  private List<CompanyRegistrationField> supplierCompanyRegistrations = new ArrayList<>();
  /**
   * The name of the supplier or merchant.
   */
  @JsonProperty("supplier_name")
  private StringField supplierName;
  /**
   * The Phone number of the supplier or merchant returned as a single string.
   */
  @JsonProperty("supplier_phone_number")
  private StringField supplierPhoneNumber;
  /**
   * List of tax lines information including: Amount, tax rate, tax base amount and tax code.
   */
  @JsonProperty("taxes")
  @JsonDeserialize(using = TaxesDeserializer.class)
  private Taxes taxes;
  /**
   * Time of purchase with 24 hours formatting (HH:MM).
   */
  @JsonProperty("time")
  private StringField time;
  /**
   * The total amount of tip and gratuity.
   */
  @JsonProperty("tip")
  private AmountField tip;
  /**
   * The total amount paid including taxes, discounts, fees, tips, and gratuity.
   */
  @JsonProperty("total_amount")
  private AmountField totalAmount;
  /**
   * The total amount excluding taxes.
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
        String.format(":Expense Category: %s%n", this.getCategory())
    );
    outStr.append(
        String.format(":Expense Sub Category: %s%n", this.getSubcategory())
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
        String.format(":Total Excluding Taxes: %s%n", this.getTotalNet())
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
