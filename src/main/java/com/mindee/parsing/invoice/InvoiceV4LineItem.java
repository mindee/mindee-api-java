package com.mindee.parsing.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.BaseField;
import lombok.Getter;


/**
 * Line items details.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceV4LineItem extends BaseField {
  @JsonProperty("product_code")
  private String productCode;
  @JsonProperty("description")
  private String description;
  @JsonProperty("quantity")
  private Double quantity;
  @JsonProperty("unit_price")
  private Double unitPrice;
  @JsonProperty("total_amount")
  private Double totalAmount;
  @JsonProperty("tax_rate")
  private Double taxRate;
  @JsonProperty("tax_amount")
  private Double taxAmount;

  /**
   * Output the line in a format suitable for inclusion in an rST table.
   */
  public String toTableLine() {
    String[] printable = this.printableValues();
    String taxRate = !printable[5].equals("") ? " (" + printable[5] + "%)" : "";
    return "| "
      + String.format("%-20s", printable[0])
      + " | "
      + String.format("%-7s", printable[1])
      + " | "
      + String.format("%-7s", printable[2])
      + " | "
      + String.format("%-8s", printable[3])
      + " | "
      + String.format("%-16s", printable[4] + taxRate)
      + " | "
      + String.format("%-36s", printable[6])
      + " |";
  }

  protected String[] printableValues() {
    String descriptionSummary = (this.description != null ? this.description : "");
    if (descriptionSummary.length() > 33) {
      descriptionSummary = descriptionSummary.substring(0, 33) + "...";
    }
    return new String[]{
      this.productCode != null ? this.productCode : "",
      this.quantity != null ? SummaryHelper.formatAmount(this.quantity) : "",
      this.unitPrice != null ? SummaryHelper.formatAmount(this.unitPrice) : "",
      this.totalAmount != null ? SummaryHelper.formatAmount(this.totalAmount) : "",
      this.taxAmount != null ? SummaryHelper.formatAmount(this.taxAmount) : "",
      this.taxRate != null ? SummaryHelper.formatAmount(this.taxRate) : "",
      this.description != null ? descriptionSummary : "",
    };
  }

  @Override
  public String toString() {
    String[] printable = this.printableValues();
    return "Product Code: "
      + printable[0]
      + ", Quantity: "
      + printable[1]
      + ", Unit Price: "
      + printable[2]
      + ", Total Amount: "
      + printable[3]
      + ", Tax Amount: "
      + printable[4]
      + ", Tax Rate (%): "
      + printable[5]
      + ", Description: "
      + printable[6].trim();
  }
}
