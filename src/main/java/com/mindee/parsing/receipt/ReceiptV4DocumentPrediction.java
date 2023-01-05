package com.mindee.parsing.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

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
  private StringField category;
  /**
   * Merchant's name as seen on the receipt.
   */
  @JsonProperty("supplier")
  private StringField supplierName;
  /**
   * List of taxes detected on the receipt.
   */
  @JsonProperty("taxes")
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
      String.format(":Locale: %s%n", this.getLocaleField()) +
        String.format(":Date: %s%n", this.getDate()) +
        String.format(":Category: %s%n", this.getCategory()) +
        String.format(":Time: %s%n", this.getTime()) +
        String.format(":Supplier name: %s%n", this.getSupplierName()) +
        String.format(":Taxes: %s%n",
          this.getTaxes().stream()
            .map(TaxField::toString)
            .collect(Collectors.joining("%n       "))) +
        String.format(":Total net: %s%n", this.getTotalNet()) +
        String.format(":Total taxes: %s%n", this.getTotalTax()) +
        String.format(":Tip: %s%n",
          this.getTip()) +
        String.format(":Total amount: %s%n",
        this.getTotalAmount());

    return SummaryHelper.cleanSummary(summary);
  }
}