package com.mindee.parsing.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.model.deserialization.PolygonDeserializer;
import com.mindee.model.geometry.Polygon;
import com.mindee.parsing.SummaryHelper;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Line items details.
 */
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class InvoiceLineItem {
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
  @JsonDeserialize(using = PolygonDeserializer.class)
  private final Polygon polygon;

  @Override
  public String toString() {
    String productCodeSummary = (this.productCode != null ? this.productCode : "");
    String quantitySummary = SummaryHelper.formatAmount(this.quantity);
    String unitPriceSummary = SummaryHelper.formatAmount(this.unitPrice);
    String totalAmountSummary = SummaryHelper.formatAmount(this.totalAmount);
    String tax = SummaryHelper.formatAmount(this.taxAmount);
    tax += this.taxRate != null ? String.format(" (%s%%)", SummaryHelper.formatAmount(this.taxRate)) : "";
    String descriptionSummary = (this.description != null ? this.description : "");
    if (descriptionSummary.length() > 32) {
      descriptionSummary = descriptionSummary.substring(0, 32) + "...";
    }

    return String.join(" | ",
      String.format("%-14s", productCodeSummary),
      String.format("%-6s", quantitySummary),
      String.format("%-7s", unitPriceSummary),
      String.format("%-8s", totalAmountSummary),
      String.format("%-16s", tax),
      descriptionSummary
    );
  }
}
