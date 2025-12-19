package com.mindee.product.billoflading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Bill of Lading API version 1.1 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillOfLadingV1Document extends Prediction {

  /**
   * A unique identifier assigned to a Bill of Lading document.
   */
  @JsonProperty("bill_of_lading_number")
  protected StringField billOfLadingNumber;
  /**
   * The shipping company responsible for transporting the goods.
   */
  @JsonProperty("carrier")
  protected BillOfLadingV1Carrier carrier;
  /**
   * The goods being shipped.
   */
  @JsonProperty("carrier_items")
  protected List<BillOfLadingV1CarrierItem> carrierItems = new ArrayList<>();
  /**
   * The party to whom the goods are being shipped.
   */
  @JsonProperty("consignee")
  protected BillOfLadingV1Consignee consignee;
  /**
   * The date when the bill of lading is issued.
   */
  @JsonProperty("date_of_issue")
  protected DateField dateOfIssue;
  /**
   * The date when the vessel departs from the port of loading.
   */
  @JsonProperty("departure_date")
  protected DateField departureDate;
  /**
   * The party to be notified of the arrival of the goods.
   */
  @JsonProperty("notify_party")
  protected BillOfLadingV1NotifyParty notifyParty;
  /**
   * The place where the goods are to be delivered.
   */
  @JsonProperty("place_of_delivery")
  protected StringField placeOfDelivery;
  /**
   * The port where the goods are unloaded from the vessel.
   */
  @JsonProperty("port_of_discharge")
  protected StringField portOfDischarge;
  /**
   * The port where the goods are loaded onto the vessel.
   */
  @JsonProperty("port_of_loading")
  protected StringField portOfLoading;
  /**
   * The party responsible for shipping the goods.
   */
  @JsonProperty("shipper")
  protected BillOfLadingV1Shipper shipper;

  @Override
  public boolean isEmpty() {
    return (this.billOfLadingNumber == null
      && this.shipper == null
      && this.consignee == null
      && this.notifyParty == null
      && this.carrier == null
      && (this.carrierItems == null || this.carrierItems.isEmpty())
      && this.portOfLoading == null
      && this.portOfDischarge == null
      && this.placeOfDelivery == null
      && this.dateOfIssue == null
      && this.departureDate == null);
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(String.format(":Bill of Lading Number: %s%n", this.getBillOfLadingNumber()));
    outStr.append(String.format(":Shipper:%n%s", this.getShipper().toFieldList()));
    outStr.append(String.format(":Consignee:%n%s", this.getConsignee().toFieldList()));
    outStr.append(String.format(":Notify Party:%n%s", this.getNotifyParty().toFieldList()));
    outStr.append(String.format(":Carrier:%n%s", this.getCarrier().toFieldList()));
    String carrierItemsSummary = "";
    if (!this.getCarrierItems().isEmpty()) {
      int[] carrierItemsColSizes = new int[] { 38, 14, 13, 18, 10, 13 };
      carrierItemsSummary = String
        .format("%n%s%n  ", SummaryHelper.lineSeparator(carrierItemsColSizes, "-"))
        + "| Description                          "
        + "| Gross Weight "
        + "| Measurement "
        + "| Measurement Unit "
        + "| Quantity "
        + "| Weight Unit "
        + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(carrierItemsColSizes, "="));
      carrierItemsSummary += SummaryHelper
        .arrayToString(this.getCarrierItems(), carrierItemsColSizes);
      carrierItemsSummary += String
        .format("%n%s", SummaryHelper.lineSeparator(carrierItemsColSizes, "-"));
    }
    outStr.append(String.format(":Items: %s%n", carrierItemsSummary));
    outStr.append(String.format(":Port of Loading: %s%n", this.getPortOfLoading()));
    outStr.append(String.format(":Port of Discharge: %s%n", this.getPortOfDischarge()));
    outStr.append(String.format(":Place of Delivery: %s%n", this.getPlaceOfDelivery()));
    outStr.append(String.format(":Date of issue: %s%n", this.getDateOfIssue()));
    outStr.append(String.format(":Departure Date: %s%n", this.getDepartureDate()));
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
