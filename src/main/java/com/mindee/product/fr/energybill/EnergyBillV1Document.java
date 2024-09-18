package com.mindee.product.fr.energybill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.AmountField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Energy Bill API version 1.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnergyBillV1Document extends Prediction {

  /**
   * The unique identifier associated with a specific contract.
   */
  @JsonProperty("contract_id")
  protected StringField contractId;
  /**
   * The unique identifier assigned to each electricity or gas consumption point. It specifies the exact location where the energy is delivered.
   */
  @JsonProperty("delivery_point")
  protected StringField deliveryPoint;
  /**
   * The date by which the payment for the energy invoice is due.
   */
  @JsonProperty("due_date")
  protected DateField dueDate;
  /**
   * The entity that consumes the energy.
   */
  @JsonProperty("energy_consumer")
  protected EnergyBillV1EnergyConsumer energyConsumer;
  /**
   * The company that supplies the energy.
   */
  @JsonProperty("energy_supplier")
  protected EnergyBillV1EnergySupplier energySupplier;
  /**
   * Details of energy consumption.
   */
  @JsonProperty("energy_usage")
  protected List<EnergyBillV1EnergyUsage> energyUsage = new ArrayList<>();
  /**
   * The date when the energy invoice was issued.
   */
  @JsonProperty("invoice_date")
  protected DateField invoiceDate;
  /**
   * The unique identifier of the energy invoice.
   */
  @JsonProperty("invoice_number")
  protected StringField invoiceNumber;
  /**
   * Information about the energy meter.
   */
  @JsonProperty("meter_details")
  protected EnergyBillV1MeterDetail meterDetails;
  /**
   * The subscription details fee for the energy service.
   */
  @JsonProperty("subscription")
  protected List<EnergyBillV1Subscription> subscription = new ArrayList<>();
  /**
   * Details of Taxes and Contributions.
   */
  @JsonProperty("taxes_and_contributions")
  protected List<EnergyBillV1TaxesAndContribution> taxesAndContributions = new ArrayList<>();
  /**
   * The total amount to be paid for the energy invoice.
   */
  @JsonProperty("total_amount")
  protected AmountField totalAmount;
  /**
   * The total amount to be paid for the energy invoice before taxes.
   */
  @JsonProperty("total_before_taxes")
  protected AmountField totalBeforeTaxes;
  /**
   * Total of taxes applied to the invoice.
   */
  @JsonProperty("total_taxes")
  protected AmountField totalTaxes;

  @Override
  public boolean isEmpty() {
    return (
      this.invoiceNumber == null
      && this.contractId == null
      && this.deliveryPoint == null
      && this.invoiceDate == null
      && this.dueDate == null
      && this.totalBeforeTaxes == null
      && this.totalTaxes == null
      && this.totalAmount == null
      && this.energySupplier == null
      && this.energyConsumer == null
      && (this.subscription == null || this.subscription.isEmpty())
      && (this.energyUsage == null || this.energyUsage.isEmpty())
      && (this.taxesAndContributions == null || this.taxesAndContributions.isEmpty())
      && this.meterDetails == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Invoice Number: %s%n", this.getInvoiceNumber())
    );
    outStr.append(
        String.format(":Contract ID: %s%n", this.getContractId())
    );
    outStr.append(
        String.format(":Delivery Point: %s%n", this.getDeliveryPoint())
    );
    outStr.append(
        String.format(":Invoice Date: %s%n", this.getInvoiceDate())
    );
    outStr.append(
        String.format(":Due Date: %s%n", this.getDueDate())
    );
    outStr.append(
        String.format(":Total Before Taxes: %s%n", this.getTotalBeforeTaxes())
    );
    outStr.append(
        String.format(":Total Taxes: %s%n", this.getTotalTaxes())
    );
    outStr.append(
        String.format(":Total Amount: %s%n", this.getTotalAmount())
    );
    outStr.append(
        String.format(":Energy Supplier:%n%s", this.getEnergySupplier().toFieldList())
    );
    outStr.append(
        String.format(":Energy Consumer:%n%s", this.getEnergyConsumer().toFieldList())
    );
    String subscriptionSummary = "";
    if (!this.getSubscription().isEmpty()) {
      int[] subscriptionColSizes = new int[]{38, 12, 12, 10, 11, 12};
      subscriptionSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(subscriptionColSizes, "-"))
          + "| Description                          "
          + "| End Date   "
          + "| Start Date "
          + "| Tax Rate "
          + "| Total     "
          + "| Unit Price "
          + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(subscriptionColSizes, "="));
      subscriptionSummary += SummaryHelper.arrayToString(this.getSubscription(), subscriptionColSizes);
      subscriptionSummary += String.format("%n%s", SummaryHelper.lineSeparator(subscriptionColSizes, "-"));
    }
    outStr.append(
        String.format(":Subscription: %s%n", subscriptionSummary)
    );
    String energyUsageSummary = "";
    if (!this.getEnergyUsage().isEmpty()) {
      int[] energyUsageColSizes = new int[]{38, 12, 12, 10, 11, 12};
      energyUsageSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(energyUsageColSizes, "-"))
          + "| Description                          "
          + "| End Date   "
          + "| Start Date "
          + "| Tax Rate "
          + "| Total     "
          + "| Unit Price "
          + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(energyUsageColSizes, "="));
      energyUsageSummary += SummaryHelper.arrayToString(this.getEnergyUsage(), energyUsageColSizes);
      energyUsageSummary += String.format("%n%s", SummaryHelper.lineSeparator(energyUsageColSizes, "-"));
    }
    outStr.append(
        String.format(":Energy Usage: %s%n", energyUsageSummary)
    );
    String taxesAndContributionsSummary = "";
    if (!this.getTaxesAndContributions().isEmpty()) {
      int[] taxesAndContributionsColSizes = new int[]{38, 12, 12, 10, 11, 12};
      taxesAndContributionsSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(taxesAndContributionsColSizes, "-"))
          + "| Description                          "
          + "| End Date   "
          + "| Start Date "
          + "| Tax Rate "
          + "| Total     "
          + "| Unit Price "
          + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(taxesAndContributionsColSizes, "="));
      taxesAndContributionsSummary += SummaryHelper.arrayToString(this.getTaxesAndContributions(), taxesAndContributionsColSizes);
      taxesAndContributionsSummary += String.format("%n%s", SummaryHelper.lineSeparator(taxesAndContributionsColSizes, "-"));
    }
    outStr.append(
        String.format(":Taxes and Contributions: %s%n", taxesAndContributionsSummary)
    );
    outStr.append(
        String.format(":Meter Details:%n%s", this.getMeterDetails().toFieldList())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
