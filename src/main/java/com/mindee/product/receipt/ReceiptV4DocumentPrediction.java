package com.mindee.product.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.field.AmountField;
import com.mindee.parsing.standard.field.ClassificationField;
import com.mindee.parsing.standard.field.DateField;
import com.mindee.parsing.standard.field.LocaleField;
import com.mindee.parsing.standard.field.StringField;
import com.mindee.parsing.standard.field.TaxField;
import com.mindee.parsing.standard.field.TaxesDeserializer;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Receipt, API version 4.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptV4DocumentPrediction {

  /**
   * Where the purchase was made, the language, and the currency.
   */
  @JsonProperty("locale")
  private LocaleField localeField;
  /**
   * The purchase date.
   */
  @JsonProperty("date")
  private DateField date;
  /**
   * The time of the purchase.
   */
  @JsonProperty("time")
  private StringField time;
  /**
   * The type of purchase among a certain list of it (see official documentation for the list).
   */
  @JsonProperty("category")
  private ClassificationField category;
  /**
   * The subcategory of purchase among a certain list of it (see official documentation for the list).
   */
  @JsonProperty("subcategory")
  private ClassificationField subCategory;
  /**
   * The type of the parsed document (see official documentation for the list).
   */
  @JsonProperty("document_type")
  private ClassificationField documentType;
  /**
   * Merchant's name as seen on the receipt.
   */
  @JsonProperty("supplier")
  private StringField supplierName;
  /**
   * List of taxes detected on the receipt.
   */
  @JsonProperty("taxes")
  @JsonDeserialize(using = TaxesDeserializer.class)
  private List<TaxField> taxes;
  /**
   * total spent including taxes, discounts, fees, tips, and gratuity.
   */
  @JsonProperty("total_amount")
  private AmountField totalAmount;
  /**
   * Total amount of the purchase excluding taxes.
   */
  @JsonProperty("total_net")
  private AmountField totalNet;
  /**
   * Total tax amount of the purchase.
   */
  @JsonProperty("total_tax")
  private AmountField totalTax;
  /**
   * Total amount of tip and gratuity.
   */
  @JsonProperty("tip")
  private AmountField tip;

  @Override
  public String toString() {

    String summary =
        String.format(":Locale: %s%n", this.getLocaleField())
        + String.format(":Date: %s%n", this.getDate())
        + String.format(":Category: %s%n", this.getCategory())
        + String.format(":Subcategory: %s%n", this.getSubCategory())
        + String.format(":Document type: %s%n", this.getDocumentType())
        + String.format(":Time: %s%n", this.getTime())
        + String.format(":Supplier name: %s%n", this.getSupplierName())
        + String.format(":Taxes: %s%n", this.taxes.toString())
        + String.format(":Total net: %s%n", this.getTotalNet())
        + String.format(":Total tax: %s%n", this.getTotalTax())
        + String.format(":Tip: %s%n", this.getTip())
        + String.format(":Total amount: %s%n", this.getTotalAmount());

    return SummaryHelper.cleanSummary(summary);
  }
}
