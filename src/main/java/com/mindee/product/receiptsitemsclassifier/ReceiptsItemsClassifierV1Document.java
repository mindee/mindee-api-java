package com.mindee.product.receiptsitemsclassifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Receipts Items Classifier, API version 1.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptsItemsClassifierV1Document extends Prediction {

  /**
   * The currency of the receipt in ISO 4217 three-letter code.
   */
  @JsonProperty("currency_code")
  private StringField currencyCode;
  /**
   * The category or type of the expense.
   */
  @JsonProperty("expense_category")
  private ClassificationField expenseCategory;
  /**
   * The ISO formatted date on which the receipt was issued.
   */
  @JsonProperty("issue_date")
  private StringField issueDate;
  /**
   * A list of fields about individual items or services being billed.
   */
  @JsonProperty("line_items")
  private List<ReceiptsItemsClassifierV1LineItem> lineItems = new ArrayList<>();
  /**
   * The address of the merchant.
   */
  @JsonProperty("merchant_address")
  private StringField merchantAddress;
  /**
   * The name of the merchant.
   */
  @JsonProperty("merchant_name")
  private StringField merchantName;
  /**
   * Whether there are several receipts on the document.
   */
  @JsonProperty("multiple_receipts")
  private StringField multipleReceipts;
  /**
   * The tip added on the receipt.
   */
  @JsonProperty("tip")
  private AmountField tip;
  /**
   * The sum of all the individual line item amounts, including taxes and discounts.
   */
  @JsonProperty("total_amount")
  private AmountField totalAmount;
  /**
   * The total tax of the bought items.
   */
  @JsonProperty("total_tax")
  private AmountField totalTax;

  @Override
  public boolean isEmpty() {
    return (
      this.multipleReceipts == null
      && this.issueDate == null
      && this.expenseCategory == null
      && this.merchantName == null
      && this.merchantAddress == null
      && this.totalAmount == null
      && this.tip == null
      && this.totalTax == null
      && this.currencyCode == null
      && (this.lineItems == null || this.lineItems.isEmpty())
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Multiple Receipts: %s%n", this.getMultipleReceipts())
    );
    outStr.append(
        String.format(":Date of Issue: %s%n", this.getIssueDate())
    );
    outStr.append(
        String.format(":Expense Type: %s%n", this.getExpenseCategory())
    );
    outStr.append(
        String.format(":Merchant: %s%n", this.getMerchantName())
    );
    outStr.append(
        String.format(":Merchant address: %s%n", this.getMerchantAddress())
    );
    outStr.append(
        String.format(":Total Amount: %s%n", this.getTotalAmount())
    );
    outStr.append(
        String.format(":Tip: %s%n", this.getTip())
    );
    outStr.append(
        String.format(":Total tax: %s%n", this.getTotalTax())
    );
    outStr.append(
        String.format(":Currency: %s%n", this.getCurrencyCode())
    );
    String lineItemsSummary = "";
    if (!this.getLineItems().isEmpty()) {
      int[] lineItemsColSizes = new int[]{21, 11, 10, 7};
      lineItemsSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(lineItemsColSizes, "-"))
          + "| Item Classification "
          + "| Item Name "
          + "| Quantity "
          + "| Price "
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
