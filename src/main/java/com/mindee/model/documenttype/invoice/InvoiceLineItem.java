package com.mindee.model.documenttype.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.geometry.Polygon;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public final class InvoiceLineItem {
  @JsonProperty("product_code")
  private final String productCode;
  @JsonProperty("description")
  private final String description;
  @JsonProperty("quantity")
  private final Double quantity;
  @JsonProperty("unit_price")
  private final Double unitPrice;
  @JsonProperty("total_amount")
  private final Double totalAmount;
  @JsonProperty("tax_rate")
  private final Double taxRate;
  @JsonProperty("tax_amount")
  private final Double taxAmount;
  @JsonProperty("confidence")
  private final Double confidence;
  @JsonProperty("page_id")
  private final Double pageId;
  @JsonProperty("polygon")
  private final Polygon polygon;

  @Override
  public String toString() {
    String productCodeSummary = (this.productCode != null ? this.productCode : "");
    String quantitySummary = (this.quantity != null ? this.quantity.toString() : "");
    String unitPriceSummary = (this.unitPrice != null ? this.unitPrice.toString() : "");
    String totalAmountSummary = (this.totalAmount != null ? this.totalAmount.toString() : "");
    String tax = (this.taxAmount != null ? this.taxAmount.toString() : "");
    tax += this.taxRate != null ? String.format(" (%f %%)", this.taxRate) : "";
    String descriptionSummary = (this.description != null ? this.description : "");
    if (descriptionSummary.length() > 32) {
      descriptionSummary = descriptionSummary.substring(0, 32) + "...";
    }

    return String.join(" | ",
      String.format("%-14s", productCodeSummary),
      String.format("%-6s", quantitySummary),
      String.format("%-7s", unitPriceSummary),
      String.format("%-8s", totalAmountSummary),
      String.format("%-14s", tax),
      descriptionSummary
    );
  }
}
